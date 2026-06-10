package com.hotelka.voicerobot.domain.model

sealed interface RobotCommand {

    data object Dance: RobotCommand
    data object Show: RobotCommand
    data object Praying: RobotCommand
    data object LayDown: RobotCommand
    data object StandUp: RobotCommand
    data object Stop: RobotCommand
    data object JumpRight: RobotCommand
    data object JumpLeft: RobotCommand
    data object Recover: RobotCommand
    data object Barrel: RobotCommand
    data object SitDown: RobotCommand
    data object Paw: RobotCommand
    data object Heart: RobotCommand



    interface CommandMapper<T> {
        fun map(command: RobotCommand): T
        fun map(command: T): RobotCommand?
    }
}