package com.hotelka.voicerobot.data.repository

import android.util.Log
import com.hotelka.voicerobot.data.mapper.RobotCommandMapper
import com.hotelka.voicerobot.data.remote.Go1ApiDataSource
import com.hotelka.voicerobot.domain.repository.RobotRepository
import java.util.logging.Logger

class RobotRepositoryImpl(
    private val robotCommandMapper: RobotCommandMapper = RobotCommandMapper,
    private val dataSource: Go1ApiDataSource
) : RobotRepository {
    override suspend fun sendCommand(command: String): Result<Unit> {
        val endpoint = robotCommandMapper.map(command)?.let { robotCommandMapper.map(it) }
        return endpoint?.let { dataSource.sendCommand(it) } ?: Result.success(Unit)
    }
}