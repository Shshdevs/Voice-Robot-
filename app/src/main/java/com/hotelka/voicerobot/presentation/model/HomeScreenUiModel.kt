package com.hotelka.voicerobot.presentation.model

data class HomeScreenUiModel(
    val micIsOn: Boolean = false,
    val bars: List<Float> = List(24) { 0.08f },
    val myRobotsExpanded: Boolean = false
)