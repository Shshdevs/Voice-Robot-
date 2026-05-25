package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.repository.MicControllerImpl
import com.hotelka.voicerobot.domain.repository.MicController
import com.hotelka.voicerobot.presentation.controllers.VoiceControlManager
import org.koin.dsl.module

val audioRecorderModule = module {
    single<MicController> { MicControllerImpl() }
    single{ VoiceControlManager(micController = get()) }
}