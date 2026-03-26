package com.hotelka.voicerobot.usecase

import com.google.common.truth.Truth.assertThat
import com.hotelka.voicerobot.data.mapper.HighCmdMapper
import com.hotelka.voicerobot.data.mapper.HighStateMockMapper
import com.hotelka.voicerobot.data.remote.datasource.RobotUdpDataSourceImpl
import com.hotelka.voicerobot.data.remote.parser.HighStateBinaryParser
import com.hotelka.voicerobot.data.repository.RobotControlSessionImpl
import com.hotelka.voicerobot.data.repository.RobotRepositoryImpl
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.Test

class SendRobotCommandUseCaseTest {

    @Test
    fun `mock exchange returns success and parsed state`() = runTest {
        val repository = RobotRepositoryImpl(
            dataSource = RobotUdpDataSourceImpl(),
            highCmdMapper = HighCmdMapper(),
            highStateMockMapper = HighStateMockMapper(),
            highStateBinaryParser = HighStateBinaryParser(),
        )

        val endpoint = RobotEndpoint(
            host = "127.0.0.1",
            port = 8082,
            kind = RobotEndpoint.Kind.Mock,
        )

        val result = repository.exchange(
            command = RobotCommand.Walk(velocityX = 0.4f),
            endpoint = endpoint,
        )

        assertThat(result.isSuccess).isTrue()

        val state = result.getOrNull()
        assertThat(state).isNotNull()
        assertThat(state!!.packetLength).isGreaterThan(0)
        assertThat(state.receivedAtMs).isGreaterThan(0L)
    }

    @Test
    fun `session publishes stream of states from mock`() = runBlocking {
        val repository = RobotRepositoryImpl(
            dataSource = RobotUdpDataSourceImpl(),
            highCmdMapper = HighCmdMapper(),
            highStateMockMapper = HighStateMockMapper(),
            highStateBinaryParser = HighStateBinaryParser(),
        )

        val session = RobotControlSessionImpl(
            robotRepository = repository,
            loopDelayMs = 50L,
            logger = ::println,
        )

        val endpoint = RobotEndpoint(
            host = "192.168.123.161",
            port = 8082,
            kind = RobotEndpoint.Kind.Real,
        )

        session.start(this, endpoint)
        session.updateCommand(RobotCommand.Walk(1.5f))

        val states = withTimeout(5_000L) {
            session.state
                .filterNotNull()
                .take(5)
                .toList()
        }

        assertThat(states).hasSize(5)
        assertThat(states.all { it.packetLength > 0 }).isTrue()
        assertThat(states.all { it.receivedAtMs > 0L }).isTrue()

        session.stop()
    }
}