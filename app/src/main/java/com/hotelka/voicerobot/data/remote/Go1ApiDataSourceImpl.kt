package com.hotelka.voicerobot.data.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Go1ApiDataSourceImpl(
    private val httpClient: HttpClient
) : Go1ApiDataSource {
    override suspend fun sendCommand(endpoint: String): Result<Unit> {
        return try {
            println("Sending command: $endpoint")

            val response = httpClient.get("http://$ROBOT_ADDRESS:$ROBOT_PORT/$VOICE_API_PATH/$endpoint")
            println(response)
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Request failed: ${response.status}"))
            }
        } catch (e: Exception) {
            println("RobotDataSource Error: ${e.message}: ${e.cause}")

            Result.failure(e)
        }
    }

    companion object {
        private const val VOICE_API_PATH = "voice"
        private const val ROBOT_ADDRESS = "192.168.123.51" //192.168.0.23 – DEBUG/ 192.168.123.51 – Робот
        private const val ROBOT_PORT = "8082"
    }
}