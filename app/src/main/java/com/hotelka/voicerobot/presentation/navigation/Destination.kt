package com.hotelka.voicerobot.presentation.navigation

sealed class Destination(val route: String) {
    object RootGraph: Destination("root")
    object Home: Destination("home")
}