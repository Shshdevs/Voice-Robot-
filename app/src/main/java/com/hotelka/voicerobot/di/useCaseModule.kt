package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.domain.usecase.SendRobotCommandUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { SendRobotCommandUseCase(robotRepository = get()) }
}