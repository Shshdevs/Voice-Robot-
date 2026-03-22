package com.hotelka.voicerobot.di

import org.koin.dsl.module

val appModule = module {
    includes(
        singlesModule, viewModelModule
    )
}