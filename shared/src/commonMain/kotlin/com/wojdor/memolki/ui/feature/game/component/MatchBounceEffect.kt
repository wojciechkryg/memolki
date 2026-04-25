package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun rememberMatchBounceScale(
    isAnimating: Boolean,
    onComplete: () -> Unit
): Float {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(MATCH_ANIMATION_DELAY)
            scale.animateTo(
                MATCH_BOUNCE_SCALE,
                spring(dampingRatio = MATCH_DAMPING_RATIO, stiffness = MATCH_STIFFNESS)
            )
            scale.animateTo(
                1f,
                spring(dampingRatio = MATCH_DAMPING_RATIO, stiffness = MATCH_STIFFNESS)
            )
            onComplete()
        }
    }
    return scale.value
}

private const val MATCH_ANIMATION_DELAY = 600L
private const val MATCH_BOUNCE_SCALE = 1.05f
private const val MATCH_DAMPING_RATIO = 0.6f
private const val MATCH_STIFFNESS = 600f
