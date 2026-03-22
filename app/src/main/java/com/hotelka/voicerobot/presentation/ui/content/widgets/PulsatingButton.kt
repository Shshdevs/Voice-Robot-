package com.hotelka.voicerobot.presentation.ui.content.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hotelka.voicerobot.R

@Preview
@Composable
fun PulsatingButton(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 96.dp,
    pulseSize: Dp = 160.dp,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    mainPulseColor: Color = MaterialTheme.colorScheme.primary,
    overlayPulseColor: Color = MaterialTheme.colorScheme.surfaceTint,
    isPulsating: Boolean = false,
    onClick: () -> Unit = {}
) {
    val transition = rememberInfiniteTransition(label = "pulse")

    val mainPulseProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mainPulse"
    )

    val overlayPulseProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                delayMillis = 750,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "overlayPulse"
    )

    Box(
        modifier = modifier.size(pulseSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    if (isPulsating) {
                        val baseRadius = buttonSize.toPx() / 2f

                        drawCircle(
                            color = overlayPulseColor,
                            radius = baseRadius * (1f + overlayPulseProgress * 1.8f),
                            alpha = 0.20f * (1f - overlayPulseProgress)
                        )
                        drawCircle(
                            color = mainPulseColor,
                            radius = baseRadius * (1f + mainPulseProgress * 1.4f),
                            alpha = 0.70f * (1f - mainPulseProgress)
                        )

                    }
                }
        )

        Box(
            modifier = Modifier
                .size(buttonSize)
                .clip(CircleShape)
                .background(buttonColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.microphone),
                contentDescription = "Micro",
                modifier = Modifier.fillMaxSize(0.7f),
                tint = iconColor
            )
        }
    }
}