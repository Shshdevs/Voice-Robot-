package com.hotelka.voicerobot.data.repository

import com.hotelka.voicerobot.data.dto.HighStateMock
import com.hotelka.voicerobot.data.mapper.HighCmdMapper
import com.hotelka.voicerobot.data.mapper.HighStateMockMapper
import com.hotelka.voicerobot.data.remote.datasource.RobotUdpDataSource
import com.hotelka.voicerobot.data.remote.packer.HighCmdPacker
import com.hotelka.voicerobot.data.remote.parser.HighStateBinaryParser
import com.hotelka.voicerobot.domain.model.HighState
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import com.hotelka.voicerobot.domain.repository.RobotRepository
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class RobotRepositoryImpl(
    private val dataSource: RobotUdpDataSource,
    private val highCmdMapper: HighCmdMapper,
    private val highStateMockMapper: HighStateMockMapper,
    private val highStateBinaryParser: HighStateBinaryParser,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) : RobotRepository {

    override suspend fun exchange(
        command: RobotCommand,
        endpoint: RobotEndpoint,
    ): Result<HighState> = runCatching {
        val highCmd = highCmdMapper.map(command)
        val payload = HighCmdPacker.pack(highCmd)

        val rawResponse = dataSource.request(
            endpoint = endpoint,
            payload = payload,
        ).getOrThrow()

        when (endpoint.kind) {
            RobotEndpoint.Kind.Mock -> parseMockJson(rawResponse)
            RobotEndpoint.Kind.Real -> parseRealOrFallback(rawResponse)
        }
    }

    private fun parseRealOrFallback(rawResponse: ByteArray): HighState {
        if (rawResponse.isEmpty()) error("Robot response is empty")

        return if (looksLikeJson(rawResponse)) {
            parseMockJson(rawResponse)
        } else {
            highStateBinaryParser.parse(rawResponse)
        }
    }

    private fun parseMockJson(rawResponse: ByteArray): HighState {
        if (rawResponse.isEmpty()) error("Mock response is empty")

        val text = rawResponse.decodeToString()

        val dto = try {
            json.decodeFromString<HighStateMock>(text)
        } catch (e: SerializationException) {
            throw IllegalStateException(
                "Failed to decode mock response as HighStateMock JSON. Raw: $text",
                e,
            )
        }

        if (!dto.ok) {
            error(buildString {
                append("Simulator returned error")
                if (dto.type.isNotBlank()) append(" [${dto.type}]")
                if (dto.meta.notes.isNotEmpty()) {
                    append(": ")
                    append(dto.meta.notes.joinToString())
                }
            })
        }

        return highStateMockMapper.map(dto)
    }

    private fun looksLikeJson(bytes: ByteArray): Boolean {
        val firstNonWhitespace = bytes
            .asSequence()
            .map { it.toInt().toChar() }
            .firstOrNull { !it.isWhitespace() }

        return firstNonWhitespace == '{' || firstNonWhitespace == '['
    }
}