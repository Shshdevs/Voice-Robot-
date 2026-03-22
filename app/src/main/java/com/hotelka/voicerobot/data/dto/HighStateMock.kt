package com.hotelka.voicerobot.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//Mock HeightState для работы с тестовым окружением

@Serializable
data class HighStateMock(
    val type: String,
    val ok: Boolean,

    val head: List<Int> = emptyList(),
    val levelFlag: Int = 0,
    val frameReserve: Int = 0,
    @SerialName("SN")
    val sn: List<Long> = emptyList(),
    val version: List<Long> = emptyList(),
    val bandWidth: Int = 0,

    val imu: ImuState = ImuState(),
    val motorState: List<MotorState> = emptyList(),
    val bms: BmsState = BmsState(),

    val footForce: List<Int> = emptyList(),
    val footForceEst: List<Int> = emptyList(),

    val mode: Int = 0,
    val progress: Float = 0f,
    val gaitType: Int = 0,
    val footRaiseHeight: Float = 0f,
    val position: List<Float> = emptyList(),
    val bodyHeight: Float = 0f,
    val velocity: List<Float> = emptyList(),
    val yawSpeed: Float = 0f,

    val rangeObstacle: List<Float> = emptyList(),
    val footPosition2Body: List<FootVector> = emptyList(),
    val footSpeed2Body: List<FootVector> = emptyList(),
    val wirelessRemote: List<Int> = emptyList(),

    val reserve: Int = 0,
    val crc: Int = 0,

    val meta: Meta = Meta(),
) {

    @Serializable
    data class ImuState(
        val quaternion: List<Float> = emptyList(),
        val gyroscope: List<Float> = emptyList(),
        val accelerometer: List<Float> = emptyList(),
        val rpy: List<Float> = emptyList(),
        val temperature: Int = 0,
    )

    @Serializable
    data class MotorState(
        val mode: Int = 0,
        val q: Float = 0f,
        val dq: Float = 0f,
        val ddq: Float = 0f,
        val tauEst: Float = 0f,
        val q_raw: Float = 0f,
        val dq_raw: Float = 0f,
        val ddq_raw: Float = 0f,
        val temperature: Int = 0,
        val reserve: List<Int> = emptyList(),
    )

    @Serializable
    data class BmsState(
        val version_h: Int = 0,
        val version_l: Int = 0,
        val bms_status: Int = 0,
        @SerialName("SOC")
        val soc: Int = 0,
        val current: Int = 0,
        val cycle: Int = 0,
        @SerialName("BQ_NTC")
        val bqNtc: List<Int> = emptyList(),
        @SerialName("MCU_NTC")
        val mcuNtc: List<Int> = emptyList(),
        val cell_vol: List<Int> = emptyList(),
    )

    @Serializable
    data class FootVector(
        val x: Float = 0f,
        val y: Float = 0f,
        val z: Float = 0f,
    )

    @Serializable
    data class Meta(
        val packetLength: Int = 0,
        val receivedAtMs: Long = 0L,
        val commandCount: Int = 0,
        val crcReceived: Long = 0L,
        val crcChecked: Boolean = false,
        val notes: List<String> = emptyList(),
        val echo: Echo = Echo(),
    ) {

        @Serializable
        data class Echo(
            val mode: Int = 0,
            val gaitType: Int = 0,
            val speedLevel: Int = 0,
            val footRaiseHeightDelta: Float = 0f,
            val bodyHeightDelta: Float = 0f,
            val position: List<Float> = emptyList(),
            val euler: List<Float> = emptyList(),
            val velocity: List<Float> = emptyList(),
            val yawSpeed: Float = 0f,
            val led: List<LedColor> = emptyList(),
            val bmsOff: Int = 0,
        ) {

            @Serializable
            data class LedColor(
                val r: Int = 0,
                val g: Int = 0,
                val b: Int = 0,
            )
        }
    }
}