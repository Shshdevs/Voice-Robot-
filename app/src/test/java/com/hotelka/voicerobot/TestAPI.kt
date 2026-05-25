package com.hotelka.voicerobot

import com.hotelka.voicerobot.data.remote.Go1ApiDataSourceImpl
import com.hotelka.voicerobot.data.repository.RobotRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test

class TestAPI {
    @Test
    fun `test api server setup successfully`() = runBlocking {
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(Logging){}
        }
        val result = Go1ApiDataSourceImpl(httpClient).sendCommand("pray")
        assertTrue(result.isSuccess)
    }
}