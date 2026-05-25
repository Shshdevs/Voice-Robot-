package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.repository.RobotRepositoryImpl
import com.hotelka.voicerobot.domain.repository.RobotRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<RobotRepository> {
        RobotRepositoryImpl(
            dataSource = get()
        )
    }
}