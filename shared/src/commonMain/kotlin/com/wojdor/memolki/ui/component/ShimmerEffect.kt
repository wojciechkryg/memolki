package com.wojdor.memolki.ui.component

import com.wojdor.memolki.ui.theme.AppColors

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerEffect(): Modifier = composed {
    val fontColor = AppColors.Font
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -SHIMMER_WIDTH_FRACTION,
        targetValue = 1f + SHIMMER_WIDTH_FRACTION,
        animationSpec = infiniteRepeatable(
            animation = tween(SHIMMER_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    drawWithContent {
        drawContent()
        val shimmerCenter = shimmerOffset * size.width
        val shimmerW = SHIMMER_WIDTH_FRACTION * size.width
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    fontColor.copy(alpha = SHIMMER_ALPHA),
                    Color.Transparent
                ),
                start = Offset(shimmerCenter - shimmerW, 0f),
                end = Offset(shimmerCenter + shimmerW, size.height)
            ),
            blendMode = BlendMode.SrcAtop
        )
    }
}

private const val SHIMMER_DURATION_MS = 2500
private const val SHIMMER_WIDTH_FRACTION = 0.35f
private const val SHIMMER_ALPHA = 0.08f
