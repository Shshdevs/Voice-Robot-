package com.hotelka.voicerobot.domain.repository

import com.hotelka.voicerobot.domain.model.HighState
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint

interface RobotRepository {

    suspend fun exchange(
        command: RobotCommand,
        endpoint: RobotEndpoint,
    ): Result<HighState>
}