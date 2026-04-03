package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SparkleCanvas(
    modifier: Modifier,
    sparkles: List<SparkleState>,
    color: Color,
    density: Float,
    strokeWidth: Float = DEFAULT_SPARKLE_STROKE,
    cornerRadius: Float = DEFAULT_SPARKLE_CORNER
) {
    Canvas(modifier = modifier) {
        sparkles.forEach { sparkle ->
            val scale = sparkle.scale.value
            if (scale > 0f) {
                val radiusPx = sparkle.sizeDp * density * scale
                drawSparkle(
                    center = Offset(
                        sparkle.xFraction.floatValue * size.width,
                        sparkle.yFraction.floatValue * size.height
                    ),
                    radius = radiusPx,
                    color = color,
                    strokeWidth = strokeWidth * density,
                    rotation = sparkle.rotation,
                    cornerRadius = cornerRadius * density
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
    cornerRadius: Float
) {
    val path = Path()
    val innerRadius = radius * SPARKLE_INNER_RATIO
    val stepAngle = PI / SPARKLE_POINTS
    for (i in 0 until SPARKLE_POINTS) {
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

class SparkleState(
    val xFraction: MutableFloatState,
    val yFraction: MutableFloatState,
    val sizeDp: Float,
    val rotation: Float,
    val delayMs: Long,
    val cycleDurationMs: Int,
    val scale: Animatable<Float, *>
)

private const val SPARKLE_POINTS = 4
private const val SPARKLE_INNER_RATIO = 0.2f
const val DEFAULT_SPARKLE_STROKE = 1.5f
const val DEFAULT_SPARKLE_CORNER = 4f
