package com.hotelka.voicerobot.domain.usecase

import com.hotelka.voicerobot.domain.model.HighState
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import com.hotelka.voicerobot.domain.repository.RobotRepository

class SendRobotCommandUseCase(
    private val robotRepository: RobotRepository,
) {
    suspend operator fun invoke(command: RobotCommand, endpoint: RobotEndpoint): Result<HighState> = robotRepository.sendCommand(command, endpoint)
}