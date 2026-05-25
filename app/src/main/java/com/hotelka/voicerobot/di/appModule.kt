package com.hotelka.voicerobot.di

import org.koin.dsl.module

val appModule = module {
    includes(
        serializationModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        audioRecorderModule,
        viewModelModule
    )
}