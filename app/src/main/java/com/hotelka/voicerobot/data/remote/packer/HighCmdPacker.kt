package com.hotelka.voicerobot.data.remote.packer

import com.hotelka.voicerobot.domain.model.HighCmd
import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
object HighCmdPacker {
    const val PACKET_SIZE_BYTES: Int = 129

    fun pack(cmd: HighCmd): ByteArray {
        val buffer = ByteBuffer
            .allocate(PACKET_SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)

        // head[2]
        buffer.putUByte(cmd.head[0])
        buffer.putUByte(cmd.head[1])

        // levelFlag
        buffer.putUByte(cmd.levelFlag)

        // frameReserve
        buffer.putUByte(cmd.frameReserve)

        // SN[2]
        buffer.putUInt(cmd.sn[0])
        buffer.putUInt(cmd.sn[1])

        // version[2]
        buffer.putUInt(cmd.version[0])
        buffer.putUInt(cmd.version[1])

        // bandWidth
        buffer.putUShort(cmd.bandWidth)

        // mode
        buffer.putUByte(cmd.mode)

        // gaitType
        buffer.putUByte(cmd.gaitType)

        // speedLevel
        buffer.putUByte(cmd.speedLevel)

        // footRaiseHeight
        buffer.putFloat(cmd.footRaiseHeight)

        // bodyHeight
        buffer.putFloat(cmd.bodyHeight)

        // position[2]
        buffer.putFloat(cmd.position[0])
        buffer.putFloat(cmd.position[1])

        // euler[3]
        buffer.putFloat(cmd.euler[0])
        buffer.putFloat(cmd.euler[1])
        buffer.putFloat(cmd.euler[2])

        // velocity[2]
        buffer.putFloat(cmd.velocity[0])
        buffer.putFloat(cmd.velocity[1])

        // yawSpeed
        buffer.putFloat(cmd.yawSpeed)

        // BmsCmd: off + reserve[3]
        buffer.putUByte(cmd.bms.off)
        buffer.putUByte(cmd.bms.reserve[0])
        buffer.putUByte(cmd.bms.reserve[1])
        buffer.putUByte(cmd.bms.reserve[2])

        // led[4] => 12 bytes
        repeat(4) { index ->
            val led = cmd.led[index]
            buffer.putUByte(led.r)
            buffer.putUByte(led.g)
            buffer.putUByte(led.b)
        }

        // wirelessRemote[40]
        repeat(40) { index ->
            buffer.putUByte(cmd.wirelessRemote[index])
        }

        // reserve
        buffer.putUInt(cmd.reserve)

        // crc
        buffer.putUInt(cmd.crc)

        check(buffer.position() == PACKET_SIZE_BYTES) {
            "Packed HighCmd has wrong size: ${buffer.position()} bytes, expected $PACKET_SIZE_BYTES"
        }

        return buffer.array()
    }

    private fun ByteBuffer.putUByte(value: UByte) {
        put(value.toByte())
    }

    private fun ByteBuffer.putUShort(value: UShort) {
        putShort(value.toShort())
    }

    private fun ByteBuffer.putUInt(value: UInt) {
        putInt(value.toInt())
    }
}