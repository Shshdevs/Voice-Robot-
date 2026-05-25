package com.hotelka.voicerobot.domain.repository

import com.hotelka.voicerobot.domain.model.RobotCommand

interface RobotRepository {
    suspend fun sendCommand(command: String): Result<Unit>
}