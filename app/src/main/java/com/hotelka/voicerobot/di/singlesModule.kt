package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.repository.MicControllerImpl
import com.hotelka.voicerobot.domain.repository.MicController
import com.hotelka.voicerobot.presentation.controllers.MicManager
import org.koin.dsl.module

val singlesModule = module {
    single<MicController> { MicControllerImpl() }
    single<MicManager> { MicManager(micController = get()) }
}