package com.hotelka.voicerobot.data.remote.packer

import java.util.zip.CRC32

object UnitreeCrc {
    fun crc32(packetWithoutCrc: ByteArray): UInt {
        val crc32 = CRC32()
        crc32.update(packetWithoutCrc, 0, packetWithoutCrc.size)
        return crc32.value.toUInt()
    }
}