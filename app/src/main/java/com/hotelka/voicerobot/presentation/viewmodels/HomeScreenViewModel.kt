package com.hotelka.voicerobot.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelka.voicerobot.domain.repository.RobotRepository
import com.hotelka.voicerobot.presentation.controllers.VoiceControlManager
import com.hotelka.voicerobot.presentation.events.HomeScreenEvents
import com.hotelka.voicerobot.presentation.model.HomeScreenUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val robotRepository: RobotRepository,
    private val voiceControlManager: VoiceControlManager
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
                            startCollectingBars()
                        }
                    }
                    uiModel.copy(micIsOn = !uiModel.micIsOn)
                }
            }
        }
    }

    private suspend fun startCollectingBars() {
        combine(
            voiceControlManager.commandRecognized,
            voiceControlManager.barHeights
        ) { command, barHeights ->
            HomeScreenUiModel(command = command, bars = barHeights)
        }.collect { newModel ->
            val command = newModel.command.let { it.partialResult.partial.ifBlank { it.finalResult.text.ifBlank { it.recognitionResult.text.ifBlank { null } } } }
            if (newModel.command != _homeScreenUiModel.value.command && command != null) onCommandChange(command)
            _homeScreenUiModel.update { uiModel ->
                uiModel.copy(
                    command = newModel.command,
                    bars = newModel.bars
                )
            }
        }
    }

    private suspend fun onCommandChange(command: String) {
        robotRepository.sendCommand(command)
    }
}