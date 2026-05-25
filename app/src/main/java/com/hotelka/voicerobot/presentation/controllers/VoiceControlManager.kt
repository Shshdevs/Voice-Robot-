package com.hotelka.voicerobot.presentation.controllers

import com.hotelka.voicerobot.domain.repository.MicController
import com.hotelka.voicerobot.domain.repository.RobotRepository
import kotlinx.coroutines.CoroutineScope

class VoiceControlManager(
    private val micController: MicController
) {
    val commandRecognized = micController.commandRecognized
    val barHeights = micController.barHeights
    fun startListening(scope: CoroutineScope): Result<Unit> = runCatching { micController.startListening(scope) }
    fun stopListening() { micController.stopListening() }


}