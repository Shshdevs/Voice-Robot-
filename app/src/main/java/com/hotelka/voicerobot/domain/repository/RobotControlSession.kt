package com.hotelka.voicerobot.domain.repository

import com.hotelka.voicerobot.domain.model.HighState
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface RobotControlSession {
    val state: StateFlow<HighState?>
    fun start(scope: CoroutineScope, endpoint: RobotEndpoint)
    fun updateCommand(command: RobotCommand)
    fun stop()
}