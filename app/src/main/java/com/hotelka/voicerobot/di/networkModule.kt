package com.hotelka.voicerobot.di

import com.hotelka.voicerobot.data.remote.Go1ApiDataSource
import com.hotelka.voicerobot.data.remote.Go1ApiDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single <HttpClient>{ HttpClient {
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
    } }
    single<Go1ApiDataSource>{ Go1ApiDataSourceImpl(get()) }
}