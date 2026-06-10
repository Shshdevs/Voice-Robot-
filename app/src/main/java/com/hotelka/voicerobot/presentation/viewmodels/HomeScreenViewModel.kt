package com.hotelka.voicerobot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelka.voicerobot.domain.repository.RobotRepository
import com.hotelka.voicerobot.presentation.controllers.VoiceControlManager
import com.hotelka.voicerobot.presentation.events.HomeScreenEvents
import com.hotelka.voicerobot.presentation.model.HomeScreenUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.microseconds

class HomeScreenViewModel(
    private val robotRepository: RobotRepository,
    private val voiceControlManager: VoiceControlManager,
) : ViewModel() {
    private val _homeScreenUiModel = MutableStateFlow(HomeScreenUiModel())
    val homeScreenUiModel = _homeScreenUiModel.asStateFlow()


    fun onEvent(event: HomeScreenEvents) {
        when (event) {
            HomeScreenEvents.OnExpandMyRobots -> {
                _homeScreenUiModel.update { uiModel -> uiModel.copy(myRobotsExpanded = !uiModel.myRobotsExpanded) }
            }

            HomeScreenEvents.OnStartStopMicEvent -> {
                _homeScreenUiModel.update { uiModel ->
                    if (uiModel.micIsOn) {
                        voiceControlManager.stopListening()
                    } else {
                        voiceControlManager.startListening(viewModelScope)
                        viewModelScope.launch {
                            startCollect()
                        }
                    }
                    uiModel.copy(micIsOn = !uiModel.micIsOn)
                }
            }
        }
    }

    private suspend fun startCollect() {
        combine(
            voiceControlManager.commandRecognized,
            voiceControlManager.barHeights
        ) { command, barHeights ->
            Pair(command, barHeights)
        }.collect { data ->
            val commandRes = data.first
            val barHeight = data.second
            val command =
                commandRes.let {
                    it.finalResult.text.ifBlank { it.recognitionResult.text.ifBlank { it.partialResult.partial.ifBlank { null } } }
                }

            if (command != _homeScreenUiModel.value.command && command != null) {
                onCommandChange(command)
            }
            _homeScreenUiModel.update { uiModel ->
                uiModel.copy(
                    command = command ?: "",
                    bars = barHeight
                )
            }
        }
    }

    private fun onCommandChange(command: String) {
        viewModelScope.launch {
            robotRepository.sendCommand(command)
            delay(5.microseconds)
        }
    }
}