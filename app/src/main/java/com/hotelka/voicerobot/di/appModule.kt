package com.hotelka.voicerobot.di

import org.koin.dsl.module

val appModule = module {
    includes(
        serializationModule,
        protocolModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        audioRecorderModule,
        viewModelModule
    )
}