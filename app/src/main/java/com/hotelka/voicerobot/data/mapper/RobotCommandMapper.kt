package com.hotelka.voicerobot.data.mapper

import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotCommand.CommandMapper

object RobotCommandMapper : CommandMapper<String> {
    override fun map(command: RobotCommand): String {
        return when (command) {
            RobotCommand.Praying -> "pray"
            RobotCommand.LayDown -> "lay_down"
            else -> "reserve"
        }
    }

    override fun map(command: String): RobotCommand? {
        return when(command){
            in arrayOf("молиться", "молится", "молись", "мольба", "моли", "молитва") -> RobotCommand.Praying
            in arrayOf("лечь", "лежать") -> RobotCommand.LayDown
            else -> null
        }
    }
}