package com.wojdor.memolki.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.theme.AppColors

private val LINE_SPACING = 20.dp
private val LINE_STROKE = 8.dp
private const val LINE_OVERSCAN_FACTOR = 2f

@Composable
fun CardBackBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize().background(AppColors.Primary)) {
        val spacingPx = LINE_SPACING.toPx()
        val strokePx = LINE_STROKE.toPx()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxDim = maxOf(size.width, size.height) * LINE_OVERSCAN_FACTOR
        val halfCount = (maxDim / spacingPx).toInt()
        rotate(degrees = 45f) {
            for (i in -halfCount..halfCount) {
                val offset = i * spacingPx
                drawLine(
                    color = Color.White,
                    start = Offset(cx - maxDim, cy + offset),
                    end = Offset(cx + maxDim, cy + offset),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = Color.White,
                    start = Offset(cx + offset, cy - maxDim),
                    end = Offset(cx + offset, cy + maxDim),
                    strokeWidth = strokePx
                )
            }
        }
    }
}
