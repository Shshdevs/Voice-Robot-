package com.hotelka.voicerobot.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class SpeechRecognitionResult(
    val partialResult: PartialResult = PartialResult(),
    val recognitionResult: RecognitionResult = RecognitionResult(),
    val finalResult: FinalResult = FinalResult()
)
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class PartialResult(
    val partial: String = ""
)
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class FinalResult(
    val text: String = ""
)
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class RecognitionResult(
    val text: String = ""
)