package com.hotelka.voicerobot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hotelka.voicerobot.presentation.navigation.graphs.rootGraph

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destination.RootGraph.route
    ) {
        rootGraph(navController)
    }
}