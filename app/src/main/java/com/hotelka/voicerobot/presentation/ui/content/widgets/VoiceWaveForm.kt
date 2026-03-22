package com.hotelka.voicerobot.presentation.ui.content.widgets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hotelka.voicerobot.presentation.ui.anim.lerp

@Composable
fun VoiceWaveform(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    barWidth: Dp = 8.dp,
    gap: Dp = 10.dp,
    heights: List<Float>,
    minVisualHeight: Dp = 8.dp,
    maxVisualHeight: Dp = 55.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { value ->
            val animated by animateDpAsState(
                targetValue = lerp(minVisualHeight, maxVisualHeight, value),
                label = "barHeight"
            )

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(animated)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}