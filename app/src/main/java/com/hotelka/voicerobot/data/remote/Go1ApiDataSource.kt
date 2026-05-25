package com.hotelka.voicerobot.data.remote

interface Go1ApiDataSource {
    suspend fun sendCommand(endpoint: String): Result<Unit>
}