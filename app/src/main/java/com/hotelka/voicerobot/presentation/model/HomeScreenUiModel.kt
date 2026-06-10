package com.hotelka.voicerobot.presentation.model

import com.hotelka.voicerobot.data.dto.FinalResult
import com.hotelka.voicerobot.data.dto.SpeechRecognitionResult

data class HomeScreenUiModel(
    val micIsOn: Boolean = false,
    val bars: List<Float> = List(24) { 0.08f },
    val command: String = "Начните прослушивание",
    val myRobotsExpanded: Boolean = false
)