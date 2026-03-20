package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun rememberShakeOffset(
    isShaking: Boolean,
    delayMs: Long = 0L,
    onComplete: () -> Unit = {}
): Float {
    val offset = remember { Animatable(0f) }
    val amplitudePx = with(LocalDensity.current) { SHAKE_AMPLITUDE.toPx() }
    LaunchedEffect(isShaking) {
        if (isShaking) {
            if (delayMs > 0) delay(delayMs)
            offset.animateTo(0f, shakeKeyframes(amplitudePx))
            onComplete()
        }
    }
    return offset.value
}

private fun shakeKeyframes(amplitude: Float): AnimationSpec<Float> = keyframes {
    durationMillis = SHAKE_DURATION_MS
    val steps = SHAKE_OSCILLATIONS * 2
    for (i in 1..steps) {
        val progress = i.toFloat() / (steps + 1)
        val decay = 1f - progress
        val value = amplitude * decay * if (i % 2 == 1) 1f else -1f
        value at (SHAKE_DURATION_MS * progress).toInt()
    }
    0f at SHAKE_DURATION_MS
}

private val SHAKE_AMPLITUDE = 4.dp
private const val SHAKE_DURATION_MS = 250
private const val SHAKE_OSCILLATIONS = 3
