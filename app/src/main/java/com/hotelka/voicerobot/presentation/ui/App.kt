package com.hotelka.voicerobot.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hotelka.voicerobot.presentation.navigation.AppNavHost
import com.hotelka.voicerobot.presentation.ui.theme.VoiceRobotTheme

@Composable
fun App() {
    VoiceRobotTheme {
        Scaffold(topBar = {Box(Modifier.fillMaxWidth())},bottomBar = {Box(Modifier.fillMaxWidth())}) { innerPadding ->
            val navController = rememberNavController()
            Box(Modifier.padding(innerPadding)){
                AppNavHost(navController)
            }
        }
    }
}