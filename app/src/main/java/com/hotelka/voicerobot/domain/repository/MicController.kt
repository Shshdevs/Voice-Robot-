package com.hotelka.voicerobot.domain.repository

import com.hotelka.voicerobot.data.dto.SpeechRecognitionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface MicController {
    val barHeights: StateFlow<List<Float>>
    val commandRecognized: StateFlow<SpeechRecognitionResult>
    fun startListening(scope: CoroutineScope)
    fun stopListening()
}