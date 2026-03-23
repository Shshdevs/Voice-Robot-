package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.mapper.HighCmdMapper
import com.hotelka.voicerobot.data.mapper.HighStateMockMapper
import com.hotelka.voicerobot.data.remote.parser.HighStateBinaryParser
import org.koin.dsl.module

val protocolModule = module {
    single { HighCmdMapper() }
    single { HighStateMockMapper() }
    single { HighStateBinaryParser() }
}