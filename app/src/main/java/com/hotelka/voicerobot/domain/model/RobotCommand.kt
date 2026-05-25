package com.hotelka.voicerobot.domain.model

sealed interface RobotCommand {

    data object Idle : RobotCommand
    data object Praying: RobotCommand

    data object LayDown: RobotCommand
    data class Walk(
        val velocityX: Float,
        val velocityY: Float = 0f,
        val yawSpeed: Float = 0f,
        val gaitType: UByte = 1u,
        val speedLevel: UByte = 0u,
        val footRaiseHeight: Float = 0f,
        val bodyHeight: Float = 0f,
    ) : RobotCommand

    data class TurnLeft(
        val yawSpeed: Float = 0.5f,
        val gaitType: UByte = 1u,
    ) : RobotCommand

    data class StandPose(
        val bodyHeight: Float = 0f,
        val roll: Float = 0f,
        val pitch: Float = 0f,
        val yaw: Float = 0f,
    ) : RobotCommand

    interface CommandMapper<T> {
        fun map(command: RobotCommand): T
        fun map(command: T): RobotCommand?
    }
}