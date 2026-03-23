package com.hotelka.voicerobot.data.remote.datasource


import com.hotelka.voicerobot.domain.model.RobotEndpoint

interface RobotUdpDataSource {
    suspend fun request(
        endpoint: RobotEndpoint,
        payload: ByteArray,
    ): Result<ByteArray>

    suspend fun requestBurst(
        endpoint: RobotEndpoint,
        payload: ByteArray,
        repeatCount: Int = 20,
        intervalMs: Long = 30L,
        localPort: Int = 8090,
    ): Result<ByteArray>
}