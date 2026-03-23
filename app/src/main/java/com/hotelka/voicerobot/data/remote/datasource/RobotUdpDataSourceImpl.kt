package com.hotelka.voicerobot.data.remote.datasource

import com.hotelka.voicerobot.domain.model.RobotEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.BindException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class RobotUdpDataSourceImpl(
    private val soTimeoutMs: Int = 150,
    private val receiveBufferSize: Int = 64 * 1024,
) : RobotUdpDataSource {

    override suspend fun request(
        endpoint: RobotEndpoint,
        payload: ByteArray,
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            validate(endpoint, payload)

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

                receiveResponse(socket)
            }
        }.recoverCatching{ e -> return@withContext Result.failure(mapNetworkError(e))}
    }

    override suspend fun requestBurst(
        endpoint: RobotEndpoint,
        payload: ByteArray,
        repeatCount: Int,
        intervalMs: Long,
        localPort: Int,
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            validate(endpoint, payload)
            require(repeatCount > 0) { "repeatCount must be > 0" }
            require(intervalMs >= 0) { "intervalMs must be >= 0" }
            require(localPort in 1..65535) { "localPort must be in range 1..65535" }

            val address = InetAddress.getByName(endpoint.host)

            try {
                DatagramSocket(localPort).use { socket ->
                    socket.soTimeout = soTimeoutMs

                    val requestPacket = DatagramPacket(
                        payload,
                        payload.size,
                        address,
                        endpoint.port,
                    )

                    repeat(repeatCount) { attempt ->
                        socket.send(requestPacket)

                        try {
                            return@withContext Result.success(receiveResponse(socket))
                        } catch (_: SocketTimeoutException) {
                            if (attempt < repeatCount - 1 && intervalMs > 0) {
                                delay(intervalMs)
                            }
                        }
                    }

                    throw IOException(
                        "UDP timeout: no response from ${endpoint.host}:${endpoint.port} " +
                                "after $repeatCount attempts from local port $localPort",
                    )
                }
            } catch (e: BindException) {
                DatagramSocket().use { socket ->
                    socket.soTimeout = soTimeoutMs

                    val requestPacket = DatagramPacket(
                        payload,
                        payload.size,
                        address,
                        endpoint.port,
                    )

                    repeat(repeatCount) { attempt ->
                        socket.send(requestPacket)

                        try {
                            return@withContext Result.success(receiveResponse(socket))
                        } catch (_: SocketTimeoutException) {
                            if (attempt < repeatCount - 1 && intervalMs > 0) {
                                delay(intervalMs)
                            }
                        }
                    }

                    throw IOException(
                        "UDP timeout: no response from ${endpoint.host}:${endpoint.port} " +
                                "after $repeatCount attempts",
                    )
                }
            }
        }.recoverCatching{ e -> return@withContext Result.failure(mapNetworkError(e))}
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
            is SocketTimeoutException -> IOException("UDP timeout", error)
            else -> error
        }
    }
}