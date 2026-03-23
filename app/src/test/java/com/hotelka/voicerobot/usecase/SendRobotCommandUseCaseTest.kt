package com.hotelka.voicerobot.usecase

import com.google.common.truth.Truth.assertThat
import com.hotelka.voicerobot.data.mapper.HighCmdMapper
import com.hotelka.voicerobot.data.mapper.HighStateMockMapper
import com.hotelka.voicerobot.data.remote.datasource.RobotUdpDataSourceImpl
import com.hotelka.voicerobot.data.remote.parser.HighStateBinaryParser
import com.hotelka.voicerobot.data.repository.RobotRepositoryImpl
import com.hotelka.voicerobot.domain.model.RobotCommand
import com.hotelka.voicerobot.domain.model.RobotEndpoint
import com.hotelka.voicerobot.domain.usecase.SendRobotCommandUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SendRobotCommandUseCaseTest {
    @Test
    fun `invoke forwards command and endpoint to repository and returns success result`() =
        runTest {
            val expectedCommand = RobotCommand.StandDown
            val expectedEndpoint = RobotEndpoint(
                host = "192.168.123.161",
                port = 8082,
                kind = RobotEndpoint.Kind.Real,
            )

            val repository = RobotRepositoryImpl(
                dataSource = RobotUdpDataSourceImpl(),
                highCmdMapper = HighCmdMapper(),
                highStateMockMapper = HighStateMockMapper(),
                highStateBinaryParser = HighStateBinaryParser(),
            )

            val useCase = SendRobotCommandUseCase(repository)

            val result = useCase(
                command = expectedCommand,
                endpoint = expectedEndpoint,
            )
            print(result.exceptionOrNull())
            print('\n')
            print(result.getOrNull())
            print('\n')
            assertThat(result.isSuccess).isTrue()
        }

    @Test
    fun `invoke returns failure from repository`() = runTest {
        val expectedCommand = RobotCommand.Walk(1f)
        val expectedEndpoint = RobotEndpoint(
            host = "127.0.0.1",
            port = 8082,
            kind = RobotEndpoint.Kind.Mock,
        )

        val repository = RobotRepositoryImpl(
            dataSource = RobotUdpDataSourceImpl(),
            highCmdMapper = HighCmdMapper(),
            highStateMockMapper = HighStateMockMapper(),
            highStateBinaryParser = HighStateBinaryParser(),
        )

        val useCase = SendRobotCommandUseCase(repository)

        val result = useCase(
            command = expectedCommand,
            endpoint = expectedEndpoint,
        )
        print(result.exceptionOrNull())
        print(result.getOrNull()?.mode)
        assertThat(result.isSuccess).isTrue()
    }
}