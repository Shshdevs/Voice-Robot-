package com.hotelka.voicerobot.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.hotelka.voicerobot.core.ContextHolder
import com.hotelka.voicerobot.data.dto.FinalResult
import com.hotelka.voicerobot.data.dto.PartialResult
import com.hotelka.voicerobot.data.dto.RecognitionResult
import com.hotelka.voicerobot.data.dto.SpeechRecognitionResult
import com.hotelka.voicerobot.domain.exceptions.AudioRecorderNotInitialized
import com.hotelka.voicerobot.domain.exceptions.AudioRecorderPermissionNotGranted
import com.hotelka.voicerobot.domain.repository.MicController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.vosk.Model
import org.vosk.Recognizer
import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.milliseconds

class MicControllerImpl(
    private val barCount: Int = 17,
    private val minBarHeight: Float = 0.08f,
    private val maxBarHeight: Float = 1f,
) : MicController {

    private val context = ContextHolder.applicationContext
    private val _commandRecognized = MutableStateFlow(SpeechRecognitionResult())
    override val commandRecognized: StateFlow<SpeechRecognitionResult> get() = _commandRecognized.asStateFlow()

    private val _barHeights = MutableStateFlow(List(barCount) { minBarHeight })
    override val barHeights: StateFlow<List<Float>> = _barHeights

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val sampleRate = 16000

    override fun startListening(scope: CoroutineScope) {
        if (recordingJob?.isActive == true) return

        checkMicrophonePermission()

        recordingJob = scope.launch(Dispatchers.Default) {
            val model = withContext(Dispatchers.IO) {
                copyModelFromAssets(context)
            }

            val recognizer = Recognizer(
                model,
                sampleRate.toFloat(),
                GRAMMAR.trimIndent()
            )

            val minBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = (minBufferSize * 2).coerceAtLeast(2048)

            val recorder = AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            if (recorder.state != AudioRecord.STATE_INITIALIZED) {
                recorder.release()
                throw AudioRecorderNotInitialized()
            }

            audioRecord = recorder
            recorder.startRecording()

            try {
                val audioBuffer = ShortArray(bufferSize / 2)

                while (isActive) {
                    val read = recorder.read(audioBuffer, 0, audioBuffer.size)

                    if (read > 0) {
                        val amplitude = calculateRms(audioBuffer, read)
                        val normalized = normalizeAmplitude(amplitude)
                        val nextBars = buildWaveBars(normalized)
                        _barHeights.value = smoothTransition(_barHeights.value, nextBars)

                        recognizer.acceptWaveForm(audioBuffer, read)

                        val result = recognizer.let {

                            val partial = Json.decodeFromString<PartialResult>(it.partialResult)
                            val result = Json.decodeFromString<RecognitionResult>(it.result)
                            val finalResult = Json.decodeFromString<FinalResult>(it.finalResult)
                            if (partial.partial.isNotBlank() || result.text.isNotBlank() || finalResult.text.isNotBlank()) {
                                SpeechRecognitionResult(partial, result, finalResult)
                            } else {
                                _commandRecognized.value
                            }
                        }
                        _commandRecognized.update { result }
                    } else {
                        _barHeights.value = decayBars(_barHeights.value)
                    }

                    delay(16.milliseconds)
                }
            } finally {
//                recorder.stop()
                recorder.release()
                audioRecord = null
                recognizer.close()
                model.close()
            }
        }
    }

    override fun stopListening() {
        recordingJob?.cancel()
        recordingJob = null
        audioRecord?.release()
        audioRecord = null
        _barHeights.value = List(barCount) { minBarHeight }
    }

    private fun applyNoiseReduction(buffer: ShortArray): ShortArray {
        val smoothed = ShortArray(buffer.size)
        for (i in 1 until buffer.size - 1) {
            smoothed[i] = ((buffer[i - 1].toInt() + buffer[i].toInt() + buffer[i + 1].toInt()) / 3).toShort()
        }
        return smoothed
    }
    private fun calculateRms(buffer: ShortArray, size: Int): Float {
        var sum = 0.0
        for (i in 0 until size) {
            val sample = buffer[i].toDouble()
            sum += sample * sample
        }
        val mean = sum / size
        return sqrt(mean).toFloat()
    }

    private fun normalizeAmplitude(rms: Float): Float {
        val normalized = (rms / 6000f).coerceIn(0f, 1f)
        return normalized * normalized
    }

    private fun buildWaveBars(level: Float): List<Float> {
        val center = barCount / 2f

        return List(barCount) { index ->
            val distanceFromCenter = abs(index - center) / center
            val shapeFactor = 1f - (distanceFromCenter * 0.7f)
            val randomFactor = (0.85f + Math.random().toFloat() * 0.3f)
            val bar = minBarHeight + (level * shapeFactor * randomFactor)
            bar.coerceIn(minBarHeight, maxBarHeight)
        }
    }


    private fun smoothTransition(
        old: List<Float>,
        new: List<Float>,
        factor: Float = 0.25f,
    ): List<Float> {
        return old.zip(new) { previous, target ->
            previous + (target - previous) * factor
        }
    }

    private fun decayBars(
        old: List<Float>,
        factor: Float = 0.12f,
    ): List<Float> {
        return old.map { value ->
            val next = value - factor
            next.coerceAtLeast(minBarHeight)
        }
    }

    private fun checkMicrophonePermission() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        check(granted) { throw AudioRecorderPermissionNotGranted() }
    }

    private fun copyModelFromAssets(
        context: Context,
        modelName: String = "vosk-model-small-ru-0.22",
    ): Model {
        val assetManager = context.assets
        val outDir = File(context.filesDir, modelName)

        if (outDir.exists()) return Model(outDir.absolutePath)

        fun copyAssetDir(path: String, outPath: File) {
            val assets = assetManager.list(path) ?: return

            if (assets.isEmpty()) {
                assetManager.open(path).use { input ->
                    outPath.parentFile?.mkdirs()
                    outPath.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } else {
                outPath.mkdirs()
                for (file in assets) {
                    copyAssetDir("$path/$file", File(outPath, file))
                }
            }
        }

        copyAssetDir(modelName, outDir)

        return Model(outDir.absolutePath)
    }

    companion object {
        private const val GRAMMAR = """
            ["молись", "мольба",
            "лежать", "ложись",
            "бочка",
            "танцуй", "танец",
            "сердце", "сердечко", 
            "влево", "налево",
            "вправо", "направо",
            "лапа", "лапу",
            "упал", "заново",
            "шоу", "представление",
            "сидеть", "сядь",
            "встань", "поднимись", "стоять",
            "стоп", "остановись"]
        """
    }
}