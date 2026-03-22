package com.hotelka.voicerobot.presentation.ui.anim

import androidx.compose.ui.unit.Dp

fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
    return start + (stop - start) * fraction.coerceIn(0f, 1f)
}