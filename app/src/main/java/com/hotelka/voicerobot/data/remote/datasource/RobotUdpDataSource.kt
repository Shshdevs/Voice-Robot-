package com.hotelka.voicerobot.data.remote.datasource


import com.hotelka.voicerobot.domain.model.RobotEndpoint

interface RobotUdpDataSource {
    suspend fun request(
        endpoint: RobotEndpoint,
        payload: ByteArray,
    ): Result<ByteArray>
}