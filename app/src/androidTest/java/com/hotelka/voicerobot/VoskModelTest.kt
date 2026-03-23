package com.hotelka.voicerobot

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.vosk.Model
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class VoskModelTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun checkVoskModelCopies() = runTest {
        val modelDir = copyModelFromAssets(context)

        assertTrue(modelDir.exists())
        assertTrue(modelDir.isDirectory)

        val model = Model(modelDir.absolutePath)
        assertNotNull(model)
    }

    private fun copyModelFromAssets(
        context: Context,
        modelName: String = "vosk-model-small-ru-0.22"
    ): File {
        val assetManager = context.assets
        val outDir = File(context.filesDir, modelName)

        if (outDir.exists()) return outDir

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
        return outDir
    }
}