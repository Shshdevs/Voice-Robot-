package com.hotelka.voicerobot.data.mapper

import com.hotelka.voicerobot.data.dto.HighStateMock
import com.hotelka.voicerobot.domain.model.HighState

class HighStateMockMapper {
    fun map(from: HighStateMock): HighState {
        return HighState(
            mode = from.mode,
            gaitType = from.gaitType,
            progress = from.progress,

            bodyHeight = from.bodyHeight,
            footRaiseHeight = from.footRaiseHeight,

            position = from.position.toVec3(),
            velocity = from.velocity.toVec3(),
            yawSpeed = from.yawSpeed,

            imu = HighState.Imu(
                quaternion = from.imu.quaternion,
                gyroscope = from.imu.gyroscope.toVec3(),
                accelerometer = from.imu.accelerometer.toVec3(),
                rpy = from.imu.rpy.toVec3(),
                temperature = from.imu.temperature,
            ),

            battery = HighState.Battery(
                soc = from.bms.soc,
                current = from.bms.current,
                cycle = from.bms.cycle,
                bmsStatus = from.bms.bms_status,
                cellVol = from.bms.cell_vol,
                bqNtc = from.bms.bqNtc,
                mcuNtc = from.bms.mcuNtc,
            ),

            footForce = from.footForce,
            footForceEst = from.footForceEst,

            footPosition2Body = from.footPosition2Body.map {
                HighState.Vec3(it.x, it.y, it.z)
            },
            footSpeed2Body = from.footSpeed2Body.map {
                HighState.Vec3(it.x, it.y, it.z)
            },

            wirelessRemote = from.wirelessRemote,

            packetLength = from.meta.packetLength,
            receivedAtMs = from.meta.receivedAtMs,
            commandCount = from.meta.commandCount,
            crc = from.crc,
        )
    }

    private fun List<Float>.toVec3(): HighState.Vec3 {
        return HighState.Vec3(
            x = getOrElse(0) { 0f },
            y = getOrElse(1) { 0f },
            z = getOrElse(2) { 0f },
        )
    }
}