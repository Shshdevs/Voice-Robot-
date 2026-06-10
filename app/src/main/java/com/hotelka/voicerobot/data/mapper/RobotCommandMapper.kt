package com.hotelka.voicerobot.data.mapper

import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotCommand.CommandMapper

object RobotCommandMapper : CommandMapper<String> {
    override fun map(command: RobotCommand): String {
        return when (command) {
            RobotCommand.Praying -> "pray"
            RobotCommand.LayDown -> "lay_down"
            RobotCommand.Barrel -> "barrel"
            RobotCommand.Dance -> "dance"
            RobotCommand.Heart -> "heart"
            RobotCommand.JumpLeft -> "jump_left"
            RobotCommand.JumpRight -> "jump_right"
            RobotCommand.Paw -> "paw"
            RobotCommand.Recover -> "recover"
            RobotCommand.Show -> "show"
            RobotCommand.SitDown -> "sit_down"
            RobotCommand.StandUp -> "stand_up"
            RobotCommand.Stop -> "stop"
        }
    }

    override fun map(command: String): RobotCommand? {
        return when (command) {
            in arrayOf("молись", "мольба") -> RobotCommand.Praying
            in arrayOf("лежать", "ложись") -> RobotCommand.LayDown
            in arrayOf("бочка") -> RobotCommand.Barrel
            in arrayOf("танцуй", "танец") -> RobotCommand.Dance
            in arrayOf("сердце", "сердечко") -> RobotCommand.Heart
            in arrayOf("влево", "налево") -> RobotCommand.JumpLeft
            in arrayOf("вправо", "вправо") -> RobotCommand.JumpRight
            in arrayOf("лапа", "лапу") -> RobotCommand.Paw
            in arrayOf("упал", "заново") -> RobotCommand.Recover
            in arrayOf("шоу", "представление") -> RobotCommand.Show
            in arrayOf("сидеть", "сядь") -> RobotCommand.SitDown
            in arrayOf("встань", "поднимись", "стоять") -> RobotCommand.StandUp
            in arrayOf("стоп", "остановись") -> RobotCommand.Stop
            else -> null
        }
    }
}