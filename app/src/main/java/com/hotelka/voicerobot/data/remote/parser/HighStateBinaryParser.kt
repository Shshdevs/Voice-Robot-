package com.hotelka.voicerobot.data.remote.parser

import com.hotelka.voicerobot.domain.model.HighState
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HighStateBinaryParser {

    fun parse(packet: ByteArray): HighState {
        require(packet.size >= PACKET_SIZE_BYTES) { "Invalid HighState size: got ${packet.size} bytes, expected at least $PACKET_SIZE_BYTES" }

        val buffer = ByteBuffer
            .wrap(packet)
            .order(ByteOrder.LITTLE_ENDIAN)

        readUByte(buffer) // head[0]
        readUByte(buffer) // head[1]
        readUByte(buffer) // levelFlag
        readUByte(buffer) // frameReserve
        readUInt(buffer)  // SN[0]
        readUInt(buffer)  // SN[1]
        readUInt(buffer)  // version[0]
        readUInt(buffer)  // version[1]
        readUShort(buffer) // bandWidth

        val imu = parseImu(buffer)

        repeat(MOTOR_COUNT) {
            skipMotorState(buffer)
        }

        val battery = parseBmsState(buffer)

        val footForce = List(FOOT_COUNT) { readShort(buffer).toInt() }
        val footForceEst = List(FOOT_COUNT) { readShort(buffer).toInt() }

        val mode = readUByte(buffer)
        print(mode)
        val progress = buffer.float
        val gaitType = readUByte(buffer)
        val footRaiseHeight = buffer.float

        val position = HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )

        val bodyHeight = buffer.float

        val velocity = HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )

        val yawSpeed = buffer.float

        repeat(4) { buffer.float }

        val footPosition2Body = List(FOOT_COUNT) { parseCartesian(buffer) }
        val footSpeed2Body = List(FOOT_COUNT) { parseCartesian(buffer) }

        val wirelessRemote = List(WIRELESS_REMOTE_SIZE) { readUByte(buffer) }

        readUInt(buffer) // reserve
        val crc = readUInt(buffer).toInt()

        return HighState(
            mode = mode,
            gaitType = gaitType,
            progress = progress,
            bodyHeight = bodyHeight,
            footRaiseHeight = footRaiseHeight,
            position = position,
            velocity = velocity,
            yawSpeed = yawSpeed,
            imu = imu,
            battery = battery,
            footForce = footForce,
            footForceEst = footForceEst,
            footPosition2Body = footPosition2Body,
            footSpeed2Body = footSpeed2Body,
            wirelessRemote = wirelessRemote,
            packetLength = packet.size,
            receivedAtMs = System.currentTimeMillis(),
            commandCount = 0,
            crc = crc,
        )
    }

    private fun parseImu(buffer: ByteBuffer): HighState.Imu {
        val quaternion = List(4) { buffer.float }

        val gyroscope = HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )

        val accelerometer = HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )

        val rpy = HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )

        val temperature = buffer.get().toInt()

        return HighState.Imu(
            quaternion = quaternion,
            gyroscope = gyroscope,
            accelerometer = accelerometer,
            rpy = rpy,
            temperature = temperature,
        )
    }

    private fun parseBmsState(buffer: ByteBuffer): HighState.Battery {
        val versionH = readUByte(buffer) // not stored in domain
        val versionL = readUByte(buffer) // not stored in domain
        val bmsStatus = readUByte(buffer)
        val soc = readUByte(buffer)
        val current = buffer.int
        val cycle = readUShort(buffer)
        val bqNtc = List(2) { buffer.get().toInt() }
        val mcuNtc = List(2) { buffer.get().toInt() }
        val cellVol = List(10) { readUShort(buffer) }

        return HighState.Battery(
            soc = soc,
            current = current,
            cycle = cycle,
            bmsStatus = bmsStatus,
            cellVol = cellVol,
            bqNtc = bqNtc,
            mcuNtc = mcuNtc,
        )
    }

    private fun parseCartesian(buffer: ByteBuffer): HighState.Vec3 {
        return HighState.Vec3(
            x = buffer.float,
            y = buffer.float,
            z = buffer.float,
        )
    }

    private fun skipMotorState(buffer: ByteBuffer) {
        readUByte(buffer) // mode
        buffer.float // q
        buffer.float // dq
        buffer.float // ddq
        buffer.float // tauEst
        buffer.float // q_raw
        buffer.float // dq_raw
        buffer.float // ddq_raw
        buffer.get() // temperature
        readUInt(buffer) // reserve[0]
        readUInt(buffer) // reserve[1]
    }

    private fun readUByte(buffer: ByteBuffer): Int {
        return buffer.get().toInt() and 0xFF
    }

    private fun readUShort(buffer: ByteBuffer): Int {
        return buffer.short.toInt() and 0xFFFF
    }

    private fun readUInt(buffer: ByteBuffer): Long {
        return buffer.int.toLong() and 0xFFFF_FFFFL
    }

    private fun readShort(buffer: ByteBuffer): Short {
        return buffer.short
    }

    companion object {
        const val PACKET_SIZE_BYTES: Int = 1083
        private const val MOTOR_COUNT = 20
        private const val FOOT_COUNT = 4
        private const val WIRELESS_REMOTE_SIZE = 40
    }
}