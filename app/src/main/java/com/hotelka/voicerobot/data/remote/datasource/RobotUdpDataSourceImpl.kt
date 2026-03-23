package com.hotelka.voicerobot.data.remote.datasource

import com.hotelka.voicerobot.domain.model.RobotEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class RobotUdpDataSourceImpl(
    private val soTimeoutMs: Int = 1500,
    private val receiveBufferSize: Int = 64 * 1024,
) : RobotUdpDataSource {

    override suspend fun request(
        endpoint: RobotEndpoint,
        payload: ByteArray,
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            require(payload.isNotEmpty()) { "UDP payload must not be empty" }
            require(endpoint.host.isNotBlank()) { "Robot host must not be blank" }
            require(endpoint.port in 1..65535) { "Robot port must be in range 1..65535" }

            DatagramSocket().use { socket ->
                socket.soTimeout = soTimeoutMs

                val address = InetAddress.getByName(endpoint.host)
                val requestPacket = DatagramPacket(
                    payload,
                    payload.size,
                    address,
                    endpoint.port,
                )

                socket.send(requestPacket)

                val responseBuffer = ByteArray(receiveBufferSize)
                val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)

                socket.receive(responsePacket)

                responseBuffer.copyOf(responsePacket.length)
            }
        }.recoverCatching { error ->
            when (error) {
                is SocketTimeoutException -> {
                    throw IOException(
                        "UDP timeout: no response from ${endpoint.host}:${endpoint.port} within ${soTimeoutMs}ms",
                        error,
                    )
                }
                else -> throw error
            }
        }
    }
}