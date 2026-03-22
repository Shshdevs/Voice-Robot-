package com.hotelka.voicerobot.data.mapper

import com.hotelka.voicerobot.domain.model.HighCmd
import com.hotelka.voicerobot.domain.model.RobotCommand

@OptIn(ExperimentalUnsignedTypes::class)
class HighCmdMapper : RobotCommand.CommandMapper<HighCmd> {
    override fun map(command: RobotCommand): HighCmd {
        return when (command) {
            RobotCommand.Idle -> HighCmd(
                mode = 0u,
                gaitType = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            RobotCommand.ForceStand -> HighCmd(
                mode = 1u,
                gaitType = 0u,
                bodyHeight = 0f,
                euler = floatArrayOf(0f, 0f, 0f),
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            is RobotCommand.StandPose -> HighCmd(
                mode = 1u,
                gaitType = 0u,
                bodyHeight = command.bodyHeight.coerceIn(-0.13f, 0.03f),
                euler = floatArrayOf(
                    command.roll.coerceIn(-0.75f, 0.75f),
                    command.pitch.coerceIn(-0.75f, 0.75f),
                    command.yaw.coerceIn(-0.6f, 0.6f),
                ),
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            is RobotCommand.Walk -> HighCmd(
                mode = 2u,
                gaitType = command.gaitType,
                speedLevel = command.speedLevel,
                footRaiseHeight = command.footRaiseHeight.coerceIn(-0.06f, 0.03f),
                bodyHeight = command.bodyHeight.coerceIn(-0.13f, 0.03f),
                velocity = floatArrayOf(
                    command.velocityX.coerceIn(-1.0f, 1.0f),
                    command.velocityY.coerceIn(-1.0f, 1.0f),
                ),
                yawSpeed = clampYawSpeed(
                    yawSpeed = command.yawSpeed,
                    gaitType = command.gaitType,
                ),
            )

            is RobotCommand.TurnLeft -> HighCmd(
                mode = 2u,
                gaitType = command.gaitType,
                speedLevel = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = clampYawSpeed(
                    yawSpeed = command.yawSpeed,
                    gaitType = command.gaitType,
                ),
            )

            RobotCommand.StandDown -> HighCmd(
                mode = 5u,
                gaitType = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            RobotCommand.StandUp -> HighCmd(
                mode = 6u,
                gaitType = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            RobotCommand.Damping -> HighCmd(
                mode = 7u,
                gaitType = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )

            RobotCommand.Recovery -> HighCmd(
                mode = 8u,
                gaitType = 0u,
                velocity = floatArrayOf(0f, 0f),
                yawSpeed = 0f,
            )
        }
    }

    private fun clampYawSpeed(yawSpeed: Float, gaitType: UByte): Float {
        return when (gaitType.toInt()) {
            3 -> yawSpeed.coerceIn(-0.7f, 0.7f)
            else -> yawSpeed.coerceIn(-4.0f, 4.0f)
        }
    }
}