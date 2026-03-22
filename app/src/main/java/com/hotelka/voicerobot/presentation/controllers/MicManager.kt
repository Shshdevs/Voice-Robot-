package com.hotelka.voicerobot.presentation.controllers

import com.hotelka.voicerobot.domain.repository.MicController
import kotlinx.coroutines.CoroutineScope

class MicManager(
    private val micController: MicController
) {
    val barHeights = micController.barHeights
    fun startListening(scope: CoroutineScope): Result<Unit> = runCatching { micController.startListening(scope) }
    fun stopListening() { micController.stopListening() }
}