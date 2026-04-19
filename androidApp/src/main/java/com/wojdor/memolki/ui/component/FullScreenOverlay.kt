package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity

private const val OVERLAY_FADE_MS = 300

@Composable
fun FullScreenOverlay(
    isVisible: Boolean,
    onShown: () -> Unit = {}
) {
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = OVERLAY_FADE_MS),
        finishedListener = { value -> if (value == 1f) onShown() }
    )
    if (overlayAlpha > 0f) {
        val density = LocalDensity.current
        val topInset = WindowInsets.systemBars.getTop(density)
        val bottomInset = WindowInsets.systemBars.getBottom(density)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .extendBehindSystemBars(topInset, bottomInset)
                .graphicsLayer { alpha = overlayAlpha }
                .background(Color.Black)
        )
    }
}

private fun Modifier.extendBehindSystemBars(topInset: Int, bottomInset: Int) =
    layout { measurable, constraints ->
        val fullHeight = constraints.maxHeight + topInset + bottomInset
        val placeable = measurable.measure(
            constraints.copy(minHeight = fullHeight, maxHeight = fullHeight)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.place(0, -topInset)
        }
    }
