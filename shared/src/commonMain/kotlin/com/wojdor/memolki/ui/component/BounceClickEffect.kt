package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

fun Modifier.bounceClickEffect(): Modifier = composed {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                scope.launch {
                    scale.animateTo(PRESS_SCALE, tween(PRESS_DURATION_MS))
                }
                waitForUpOrCancellation()
                scope.launch {
                    scale.animateTo(
                        1f,
                        spring(dampingRatio = SPRING_DAMPING_RATIO)
                    )
                }
            }
        }
}

private const val PRESS_SCALE = 0.98f
private const val PRESS_DURATION_MS = 200
private const val SPRING_DAMPING_RATIO = 0.9f
