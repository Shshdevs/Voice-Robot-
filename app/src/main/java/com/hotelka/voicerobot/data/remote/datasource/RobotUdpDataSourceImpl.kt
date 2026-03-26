package com.hotelka.voicerobot.data.remote.datasource

import com.hotelka.voicerobot.domain.model.RobotEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class RobotUdpDataSourceImpl(
    private val soTimeoutMs: Int = 300,
    private val receiveBufferSize: Int = 64 * 1024,
) : RobotUdpDataSource, Closeable {

    private val socket = DatagramSocket().apply {
        soTimeout = soTimeoutMs
    }

    override suspend fun request(
        endpoint: RobotEndpoint,
        payload: ByteArray,
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            validate(endpoint, payload)

            val address = InetAddress.getByName(endpoint.host)
            val requestPacket = DatagramPacket(
                payload,
                payload.size,
                address,
                endpoint.port,
            )

            socket.send(requestPacket)
            receiveResponse(socket)
        }.recoverCatching { error ->
            throw mapNetworkError(error)
        }
    }

    private fun validate(endpoint: RobotEndpoint, payload: ByteArray) {
        require(payload.isNotEmpty()) { "UDP payload must not be empty" }
        require(endpoint.host.isNotBlank()) { "Robot host must not be blank" }
        require(endpoint.port in 1..65535) { "Robot port must be in range 1..65535" }
    }

    private fun receiveResponse(socket: DatagramSocket): ByteArray {
        val responseBuffer = ByteArray(receiveBufferSize)
        val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
        socket.receive(responsePacket)
        return responseBuffer.copyOf(responsePacket.length)
    }

    private fun mapNetworkError(error: Throwable): Throwable {
        return when (error) {
            is SocketTimeoutException -> IOException("UDP timeout from robot/mock", error)
            else -> error
        }
    }

    override fun close() {
        if (!socket.isClosed) socket.close()
    }
}