package com.hotelka.voicerobot.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializationModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }
}