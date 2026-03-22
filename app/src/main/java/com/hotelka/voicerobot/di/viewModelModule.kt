package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.presentation.viewmodels.HomeScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<HomeScreenViewModel> { HomeScreenViewModel(micManager = get()) }
}