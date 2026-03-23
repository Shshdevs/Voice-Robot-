package com.hotelka.voicerobot.data.remote.packer

import com.hotelka.voicerobot.domain.model.HighCmd
import java.nio.ByteBuffer
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)
object HighCmdPacker {
    const val PACKET_SIZE_BYTES: Int = 129
    private const val CRC_SIZE_BYTES: Int = 4
    private const val PACKET_WITHOUT_CRC_BYTES: Int = PACKET_SIZE_BYTES - CRC_SIZE_BYTES

    fun pack(cmd: HighCmd): ByteArray {
        val initialized = cmd.initialized()

        val bufferWithoutCrc = ByteBuffer
            .allocate(PACKET_WITHOUT_CRC_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)

        // head[2]
        bufferWithoutCrc.putUByte(initialized.head[0])
        bufferWithoutCrc.putUByte(initialized.head[1])

        // levelFlag
        bufferWithoutCrc.putUByte(initialized.levelFlag)

        // frameReserve
        bufferWithoutCrc.putUByte(initialized.frameReserve)

        // SN[2]
        bufferWithoutCrc.putUInt(initialized.sn[0])
        bufferWithoutCrc.putUInt(initialized.sn[1])

        // version[2]
        bufferWithoutCrc.putUInt(initialized.version[0])
        bufferWithoutCrc.putUInt(initialized.version[1])

        // bandWidth
        bufferWithoutCrc.putUShort(initialized.bandWidth)

        // mode
        bufferWithoutCrc.putUByte(initialized.mode)

        // gaitType
        bufferWithoutCrc.putUByte(initialized.gaitType)

        // speedLevel
        bufferWithoutCrc.putUByte(initialized.speedLevel)

        // footRaiseHeight
        bufferWithoutCrc.putFloat(initialized.footRaiseHeight)

        // bodyHeight
        bufferWithoutCrc.putFloat(initialized.bodyHeight)

        // position[2]
        bufferWithoutCrc.putFloat(initialized.position[0])
        bufferWithoutCrc.putFloat(initialized.position[1])

        // euler[3]
        bufferWithoutCrc.putFloat(initialized.euler[0])
        bufferWithoutCrc.putFloat(initialized.euler[1])
        bufferWithoutCrc.putFloat(initialized.euler[2])

        // velocity[2]
        bufferWithoutCrc.putFloat(initialized.velocity[0])
        bufferWithoutCrc.putFloat(initialized.velocity[1])

        // yawSpeed
        bufferWithoutCrc.putFloat(initialized.yawSpeed)

        // BmsCmd
        bufferWithoutCrc.putUByte(initialized.bms.off)
        bufferWithoutCrc.putUByte(initialized.bms.reserve[0])
        bufferWithoutCrc.putUByte(initialized.bms.reserve[1])
        bufferWithoutCrc.putUByte(initialized.bms.reserve[2])

        // led[4]
        repeat(4) { index ->
            val led = initialized.led[index]
            bufferWithoutCrc.putUByte(led.r)
            bufferWithoutCrc.putUByte(led.g)
            bufferWithoutCrc.putUByte(led.b)
        }

        // wirelessRemote[40]
        repeat(40) { index ->
            bufferWithoutCrc.putUByte(initialized.wirelessRemote[index])
        }

        // reserve
        bufferWithoutCrc.putUInt(initialized.reserve)

        check(bufferWithoutCrc.position() == PACKET_WITHOUT_CRC_BYTES) {
            "Packed HighCmd without CRC has wrong size: ${bufferWithoutCrc.position()} bytes, expected $PACKET_WITHOUT_CRC_BYTES"
        }

        val body = bufferWithoutCrc.array()
        val crc = UnitreeCrc.crc32(body)

        val packet = ByteBuffer
            .allocate(PACKET_SIZE_BYTES)
            .order(ByteOrder.LITTLE_ENDIAN)

        packet.put(body)
        packet.putUInt(crc)

        val packetArray = packet.array()
        check(packetArray.size == PACKET_SIZE_BYTES) {
            "Packed HighCmd has wrong size: ${packetArray.size} bytes, expected $PACKET_SIZE_BYTES"
        }

        return packetArray
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