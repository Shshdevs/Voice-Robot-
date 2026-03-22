package com.hotelka.voicerobot.presentation.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hotelka.voicerobot.presentation.navigation.Destination
import com.hotelka.voicerobot.presentation.ui.screens.HomeScreen
import com.hotelka.voicerobot.presentation.viewmodels.HomeScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.rootGraph(navController: NavController) {
    navigation(
        route = Destination.RootGraph.route,
        startDestination = Destination.Home.route
    ) {
        composable(Destination.Home.route) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) { navController.getBackStackEntry(Destination.RootGraph.route) }
            val viewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = parentEntry)
            HomeScreen(viewModel)
        }
    }
}