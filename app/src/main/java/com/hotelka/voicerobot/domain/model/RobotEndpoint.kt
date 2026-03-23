package com.hotelka.voicerobot.domain.model

data class RobotEndpoint(
    val host: String,
    val port: Int,
    val kind: Kind = Kind.Real,
) {
    enum class Kind {
        Real,
        Mock
    }
}