package com.hotelka.voicerobot.domain.model

data class HighState(
    val mode: Int = 0,
    val gaitType: Int = 0,
    val progress: Float = 0f,

    val bodyHeight: Float = 0f,
    val footRaiseHeight: Float = 0f,

    val position: Vec3 = Vec3(),
    val velocity: Vec3 = Vec3(),
    val yawSpeed: Float = 0f,

    val imu: Imu = Imu(),
    val battery: Battery = Battery(),

    val footForce: List<Int> = emptyList(),
    val footForceEst: List<Int> = emptyList(),

    val footPosition2Body: List<Vec3> = emptyList(),
    val footSpeed2Body: List<Vec3> = emptyList(),

    val wirelessRemote: List<Int> = emptyList(),

    val packetLength: Int = 0,
    val receivedAtMs: Long = 0L,
    val commandCount: Int = 0,
    val crc: Int = 0,
) {

    data class Vec3(
        val x: Float = 0f,
        val y: Float = 0f,
        val z: Float = 0f,
    )

    data class Imu(
        val quaternion: List<Float> = emptyList(),
        val gyroscope: Vec3 = Vec3(),
        val accelerometer: Vec3 = Vec3(),
        val rpy: Vec3 = Vec3(),
        val temperature: Int = 0,
    )

    data class Battery(
        val soc: Int = 0,
        val current: Int = 0,
        val cycle: Int = 0,
        val bmsStatus: Int = 0,
        val cellVol: List<Int> = emptyList(),
        val bqNtc: List<Int> = emptyList(),
        val mcuNtc: List<Int> = emptyList(),
    )
}