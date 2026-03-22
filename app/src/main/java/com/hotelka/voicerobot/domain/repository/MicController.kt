package com.hotelka.voicerobot.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface MicController {
    val barHeights: StateFlow<List<Float>>
    fun startListening(scope: CoroutineScope)
    fun stopListening()
}