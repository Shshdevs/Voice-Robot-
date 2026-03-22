package com.hotelka.voicerobot.presentation.events

sealed class HomeScreenEvents {
    object OnStartStopMicEvent: HomeScreenEvents()
    object OnExpandMyRobots: HomeScreenEvents()
}