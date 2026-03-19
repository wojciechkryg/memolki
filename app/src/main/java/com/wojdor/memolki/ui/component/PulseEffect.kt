package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.pulseEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = PULSE_SCALE,
        animationSpec = infiniteRepeatable(
            animation = tween(PULSE_DURATION_MS),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse scale"
    )
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

private const val PULSE_SCALE = 1.06f
private const val PULSE_DURATION_MS = 1200
