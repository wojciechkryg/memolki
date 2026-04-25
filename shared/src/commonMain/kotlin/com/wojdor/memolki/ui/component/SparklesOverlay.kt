package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.wojdor.memolki.ui.theme.AppColors
import com.wojdor.memolki.ui.theme.AppTheme
import kotlin.random.Random
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SparklesOverlay(isActive: Boolean) {
    if (!isActive) return
    val density = LocalDensity.current.density
    val fontColor = AppColors.Font
    val sparkles = remember {
        val cellWidth = (1f - 2 * MARGIN) / GRID_COLUMNS
        val cellHeight = (1f - 2 * MARGIN) / GRID_ROWS
        List(OVERLAY_SPARKLE_COUNT) { index ->
            val col = index % GRID_COLUMNS
            val row = index / GRID_COLUMNS
            SparkleState(
                xFraction = mutableFloatStateOf(
                    MARGIN + col * cellWidth + Random.nextFloat() * cellWidth
                ),
                yFraction = mutableFloatStateOf(
                    MARGIN + row * cellHeight + Random.nextFloat() * cellHeight
                ),
                sizeDp = OVERLAY_SPARKLE_MIN_SIZE + Random.nextFloat() * OVERLAY_SPARKLE_SIZE_RANGE,
                rotation = Random.nextFloat() * OVERLAY_SPARKLE_ROTATION_RANGE,
                delayMs = (Random.nextFloat() * OVERLAY_SPARKLE_STAGGER_RANGE).toLong(),
                cycleDurationMs = OVERLAY_SPARKLE_SCALE_UP_MS + OVERLAY_SPARKLE_SCALE_DOWN_MS,
                scale = Animatable(0f)
            )
        }
    }
    sparkles.forEachIndexed { index, sparkle ->
        LaunchedEffect(index) {
            delay(sparkle.delayMs)
            sparkle.scale.animateTo(1f, tween(OVERLAY_SPARKLE_SCALE_UP_MS))
            sparkle.scale.animateTo(0f, tween(OVERLAY_SPARKLE_SCALE_DOWN_MS))
        }
    }
    SparkleCanvas(
        modifier = Modifier.fillMaxSize(),
        sparkles = sparkles,
        color = fontColor,
        density = density,
        strokeWidth = OVERLAY_SPARKLE_STROKE,
        cornerRadius = OVERLAY_SPARKLE_CORNER
    )
}

private const val GRID_COLUMNS = 6
private const val GRID_ROWS = 4
private const val OVERLAY_SPARKLE_COUNT = GRID_COLUMNS * GRID_ROWS
private const val OVERLAY_SPARKLE_MIN_SIZE = 5f
private const val OVERLAY_SPARKLE_SIZE_RANGE = 6f
private const val OVERLAY_SPARKLE_STAGGER_RANGE = 1500f
private const val OVERLAY_SPARKLE_SCALE_UP_MS = 400
private const val OVERLAY_SPARKLE_SCALE_DOWN_MS = 1800
private const val OVERLAY_SPARKLE_ROTATION_RANGE = 0.8f
private const val OVERLAY_SPARKLE_STROKE = 2f
private const val OVERLAY_SPARKLE_CORNER = 6f
private const val MARGIN = 0.05f

@Preview
@Composable
private fun SparklesOverlayPreview() {
    AppTheme {
        val density = LocalDensity.current.density
        val fontColor = AppColors.Font
        Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {
            val positions = listOf(
                Triple(0.12f, 0.15f, 7f),
                Triple(0.45f, 0.12f, 9f),
                Triple(0.78f, 0.2f, 11f),
                Triple(0.25f, 0.35f, 10f),
                Triple(0.6f, 0.3f, 6f),
                Triple(0.9f, 0.4f, 8f),
                Triple(0.1f, 0.55f, 12f),
                Triple(0.4f, 0.5f, 7f),
                Triple(0.7f, 0.55f, 10f),
                Triple(0.3f, 0.75f, 9f),
                Triple(0.55f, 0.7f, 11f),
                Triple(0.85f, 0.75f, 8f),
                Triple(0.15f, 0.9f, 6f),
                Triple(0.5f, 0.88f, 10f),
                Triple(0.75f, 0.92f, 7f)
            )
            positions.forEachIndexed { index, (xFrac, yFrac, sizeDp) ->
                drawSparkle(
                    center = Offset(xFrac * size.width, yFrac * size.height),
                    radius = sizeDp * density,
                    color = fontColor,
                    strokeWidth = OVERLAY_SPARKLE_STROKE * density,
                    rotation = index * 0.4f,
                    cornerRadius = OVERLAY_SPARKLE_CORNER * density
                )
            }
        }
    }
}
