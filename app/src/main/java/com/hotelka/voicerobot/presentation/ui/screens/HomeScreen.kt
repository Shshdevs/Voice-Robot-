package com.hotelka.voicerobot.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hotelka.voicerobot.presentation.ui.content.HomeScreenContent
import com.hotelka.voicerobot.presentation.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {
    val homeUiModel by viewModel.homeScreenUiModel.collectAsState()
    HomeScreenContent(
        homeUiModel = homeUiModel,
        onEvent = viewModel::onEvent
    )
}