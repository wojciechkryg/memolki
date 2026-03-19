package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SparklesOverlay(isActive: Boolean) {
    if (!isActive) return
    val density = LocalDensity.current.density
    val fontColor = colorResource(R.color.font)
    val sparkles = remember {
        val cellWidth = (1f - 2 * MARGIN) / GRID_COLUMNS
        val cellHeight = (1f - 2 * MARGIN) / GRID_ROWS
        List(SPARKLE_COUNT) { index ->
            val col = index % GRID_COLUMNS
            val row = index / GRID_COLUMNS
            SparkleState(
                xFraction = MARGIN + col * cellWidth + Random.nextFloat() * cellWidth,
                yFraction = MARGIN + row * cellHeight + Random.nextFloat() * cellHeight,
                sizeDp = SPARKLE_MIN_SIZE_DP + Random.nextFloat() * SPARKLE_SIZE_RANGE_DP,
                delayMs = (Random.nextFloat() * SPARKLE_STAGGER_RANGE).toLong(),
                rotation = Random.nextFloat() * SPARKLE_ROTATION_RANGE,
                scale = Animatable(0f)
            )
        }
    }
    sparkles.forEachIndexed { index, sparkle ->
        LaunchedEffect(index) {
            delay(sparkle.delayMs)
            sparkle.scale.animateTo(1f, tween(SPARKLE_SCALE_UP_MS))
            sparkle.scale.animateTo(0f, tween(SPARKLE_SCALE_DOWN_MS))
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        sparkles.forEach { sparkle ->
            val scale = sparkle.scale.value
            if (scale > 0f) {
                val radiusPx = sparkle.sizeDp * density * scale
                drawSparkle(
                    center = Offset(
                        sparkle.xFraction * size.width,
                        sparkle.yFraction * size.height
                    ),
                    radius = radiusPx,
                    color = fontColor,
                    strokeWidth = SPARKLE_STROKE_WIDTH * density,
                    rotation = sparkle.rotation,
                    cornerRadius = SPARKLE_CORNER_RADIUS * density
                )
            }
        }
    }
}

private fun DrawScope.drawSparkle(
    center: Offset,
    radius: Float,
    color: Color,
    strokeWidth: Float,
    rotation: Float,
    cornerRadius: Float,
    points: Int = SPARKLE_POINTS
) {
    val path = Path()
    val innerRadius = radius * SPARKLE_INNER_RATIO
    val stepAngle = PI / points
    for (i in 0 until points) {
        val tipAngle = rotation + (PI / 2) + (i * 2 * stepAngle)
        val valleyAngle = tipAngle + stepAngle
        val nextTipAngle = tipAngle + 2 * stepAngle
        val tipX = center.x + (radius * cos(tipAngle)).toFloat()
        val tipY = center.y - (radius * sin(tipAngle)).toFloat()
        val valleyX = center.x + (innerRadius * cos(valleyAngle)).toFloat()
        val valleyY = center.y - (innerRadius * sin(valleyAngle)).toFloat()
        val nextTipX = center.x + (radius * cos(nextTipAngle)).toFloat()
        val nextTipY = center.y - (radius * sin(nextTipAngle)).toFloat()
        if (i == 0) path.moveTo(tipX, tipY)
        path.quadraticTo(valleyX, valleyY, nextTipX, nextTipY)
    }
    path.close()
    drawPath(
        path,
        color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.cornerPathEffect(cornerRadius)
        )
    )
}

private class SparkleState(
    val xFraction: Float,
    val yFraction: Float,
    val sizeDp: Float,
    val delayMs: Long,
    val rotation: Float,
    val scale: Animatable<Float, *>
)

private const val GRID_COLUMNS = 6
private const val GRID_ROWS = 4
private const val SPARKLE_COUNT = GRID_COLUMNS * GRID_ROWS
private const val SPARKLE_MIN_SIZE_DP = 5f
private const val SPARKLE_SIZE_RANGE_DP = 6f
private const val SPARKLE_STAGGER_RANGE = 1500f
private const val SPARKLE_SCALE_UP_MS = 400
private const val SPARKLE_SCALE_DOWN_MS = 1800
private const val SPARKLE_POINTS = 4
private const val SPARKLE_INNER_RATIO = 0.2f
private const val SPARKLE_STROKE_WIDTH = 2f
private const val SPARKLE_CORNER_RADIUS = 6f
private const val SPARKLE_ROTATION_RANGE = 0.8f
private const val MARGIN = 0.05f

@Preview(showBackground = true)
@Composable
private fun SparklesOverlayPreview() {
    AppTheme {
        val density = LocalDensity.current.density
        val fontColor = colorResource(R.color.font)
        Canvas(modifier = Modifier.fillMaxSize()) {
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
                    strokeWidth = SPARKLE_STROKE_WIDTH * density,
                    rotation = index * 0.4f,
                    cornerRadius = SPARKLE_CORNER_RADIUS * density
                )
            }
        }
    }
}
