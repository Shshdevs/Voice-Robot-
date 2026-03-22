package com.hotelka.voicerobot.domain.model

@OptIn(ExperimentalUnsignedTypes::class)
data class HighCmd(
    val head: UByteArray = ubyteArrayOf(0xFEu, 0xEFu),
    val levelFlag: UByte = 0u,
    val frameReserve: UByte = 0u,
    val sn: UIntArray = uintArrayOf(0u, 0u),
    val version: UIntArray = uintArrayOf(0u, 0u),
    val bandWidth: UShort = 0u,

    val mode: UByte,
    val gaitType: UByte = 0u,
    val speedLevel: UByte = 0u,

    val footRaiseHeight: Float = 0f,
    val bodyHeight: Float = 0f,

    val position: FloatArray = floatArrayOf(0f, 0f),
    val euler: FloatArray = floatArrayOf(0f, 0f, 0f),
    val velocity: FloatArray = floatArrayOf(0f, 0f),
    val yawSpeed: Float = 0f,

    val bms: BmsCmd = BmsCmd(),
    val led: List<LedColor> = List(4) { LedColor() },
    val wirelessRemote: UByteArray = UByteArray(40) { 0u },

    val reserve: UInt = 0u,
    val crc: UInt = 0u,
) {

    init {
        require(head.size == 2) { "head must contain 2 bytes" }
        require(sn.size == 2) { "sn must contain 2 uints" }
        require(version.size == 2) { "version must contain 2 uints" }
        require(position.size == 2) { "position must contain 2 floats" }
        require(euler.size == 3) { "euler must contain 3 floats" }
        require(velocity.size == 2) { "velocity must contain 2 floats" }
        require(led.size == 4) { "led must contain 4 colors" }
        require(wirelessRemote.size == 40) { "wirelessRemote must contain 40 bytes" }
    }

    data class BmsCmd(
        val off: UByte = 0u,
        val reserve: UByteArray = UByteArray(3) { 0u },
    ) {
        init {
            require(reserve.size == 3) { "BmsCmd.reserve must contain 3 bytes" }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BmsCmd) return false

            return off == other.off &&
                    reserve.contentEquals(other.reserve)
        }

        override fun hashCode(): Int {
            var result = off.hashCode()
            result = 31 * result + reserve.contentHashCode()
            return result
        }
    }

    data class LedColor(
        val r: UByte = 0u,
        val g: UByte = 0u,
        val b: UByte = 0u,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighCmd) return false

        return head.contentEquals(other.head) &&
                levelFlag == other.levelFlag &&
                frameReserve == other.frameReserve &&
                sn.contentEquals(other.sn) &&
                version.contentEquals(other.version) &&
                bandWidth == other.bandWidth &&
                mode == other.mode &&
                gaitType == other.gaitType &&
                speedLevel == other.speedLevel &&
                footRaiseHeight == other.footRaiseHeight &&
                bodyHeight == other.bodyHeight &&
                position.contentEquals(other.position) &&
                euler.contentEquals(other.euler) &&
                velocity.contentEquals(other.velocity) &&
                yawSpeed == other.yawSpeed &&
                bms == other.bms &&
                led == other.led &&
                wirelessRemote.contentEquals(other.wirelessRemote) &&
                reserve == other.reserve &&
                crc == other.crc
    }

    override fun hashCode(): Int {
        var result = head.contentHashCode()
        result = 31 * result + levelFlag.hashCode()
        result = 31 * result + frameReserve.hashCode()
        result = 31 * result + sn.contentHashCode()
        result = 31 * result + version.contentHashCode()
        result = 31 * result + bandWidth.hashCode()
        result = 31 * result + mode.hashCode()
        result = 31 * result + gaitType.hashCode()
        result = 31 * result + speedLevel.hashCode()
        result = 31 * result + footRaiseHeight.hashCode()
        result = 31 * result + bodyHeight.hashCode()
        result = 31 * result + position.contentHashCode()
        result = 31 * result + euler.contentHashCode()
        result = 31 * result + velocity.contentHashCode()
        result = 31 * result + yawSpeed.hashCode()
        result = 31 * result + bms.hashCode()
        result = 31 * result + led.hashCode()
        result = 31 * result + wirelessRemote.contentHashCode()
        result = 31 * result + reserve.hashCode()
        result = 31 * result + crc.hashCode()
        return result
    }
}