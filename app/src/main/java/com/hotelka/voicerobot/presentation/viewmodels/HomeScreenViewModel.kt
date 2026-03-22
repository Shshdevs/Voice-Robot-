package com.hotelka.voicerobot.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelka.voicerobot.presentation.controllers.MicManager
import com.hotelka.voicerobot.presentation.events.HomeScreenEvents
import com.hotelka.voicerobot.presentation.model.HomeScreenUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val micManager: MicManager
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
                        micManager.stopListening()
                    } else {
                        micManager.startListening(viewModelScope)
                        startCollectingBars()
                    }
                    uiModel.copy(micIsOn = !uiModel.micIsOn)
                }
            }
        }
    }

    private fun startCollectingBars() {
        viewModelScope.launch {
            micManager.barHeights.collect { bars ->
                _homeScreenUiModel.update { uiModel ->
                    uiModel.copy(bars = bars)
                }
            }
        }
    }

}