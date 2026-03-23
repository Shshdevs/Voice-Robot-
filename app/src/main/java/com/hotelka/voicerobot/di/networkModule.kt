package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.remote.datasource.RobotUdpDataSource
import com.hotelka.voicerobot.data.remote.datasource.RobotUdpDataSourceImpl
import org.koin.dsl.module

val networkModule = module {
    single<RobotUdpDataSource> {
        RobotUdpDataSourceImpl(
            soTimeoutMs = 1_500,
            receiveBufferSize = 64 * 1024,
        )
    }
}