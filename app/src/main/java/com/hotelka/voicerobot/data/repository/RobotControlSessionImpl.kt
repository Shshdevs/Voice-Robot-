package com.hotelka.voicerobot.data.repository

import com.hotelka.voicerobot.domain.model.HighState
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import com.hotelka.voicerobot.domain.repository.RobotControlSession
import com.hotelka.voicerobot.domain.repository.RobotRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RobotControlSessionImpl(
    private val robotRepository: RobotRepository,
    private val loopDelayMs: Long = 30L,
    private val logger: (String) -> Unit = {},
) : RobotControlSession {

    private val currentCommand = MutableStateFlow<RobotCommand>(RobotCommand.Idle)
    private val _state = MutableStateFlow<HighState?>(null)
    override val state: StateFlow<HighState?> = _state.asStateFlow()

    private var controlJob: Job? = null

    override fun start(
        scope: CoroutineScope,
        endpoint: RobotEndpoint,
    ) {
        if (controlJob?.isActive == true) return

        controlJob = scope.launch {
            logger("RobotControlSession started: $endpoint")

            while (isActive) {
                val command = currentCommand.value

                robotRepository.exchange(command, endpoint)
                    .onSuccess { state ->
                        _state.value = state
                        logger("exchange success: command=$command mode=${state.mode} gait=${state.gaitType}")
                    }
                    .onFailure { error ->
                        logger("exchange failure: command=$command error=${error.message}")
                    }

                delay(loopDelayMs)
            }
        }
    }

    override fun updateCommand(command: RobotCommand) {
        currentCommand.value = command
        logger("command updated: $command")
    }

    override fun stop() {
        logger("RobotControlSession stopping")
        currentCommand.value = RobotCommand.Idle
        controlJob?.cancel()
        controlJob = null
    }
}