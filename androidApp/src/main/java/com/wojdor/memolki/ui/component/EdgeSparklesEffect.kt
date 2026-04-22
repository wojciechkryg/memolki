package com.wojdor.memolki.ui.component

import com.wojdor.memolki.ui.theme.AppColors

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.wojdor.memolki.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun EdgeSparklesEffect(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val fontColor = AppColors.Font
    val density = LocalDensity.current.density
    val sparkles = remember {
        List(SPARKLE_COUNT) {
            val (x, y) = randomEdgePosition()
            SparkleState(
                xFraction = mutableFloatStateOf(x),
                yFraction = mutableFloatStateOf(y),
                sizeDp = SPARKLE_MIN_SIZE + Random.nextFloat() * SPARKLE_SIZE_RANGE,
                rotation = Random.nextFloat() * SPARKLE_ROTATION_RANGE,
                delayMs = (Random.nextFloat() * SPARKLE_STAGGER).toLong(),
                cycleDurationMs = SPARKLE_CYCLE_MIN + Random.nextInt(SPARKLE_CYCLE_RANGE),
                scale = Animatable(0f)
            )
        }
    }
    sparkles.forEachIndexed { index, sparkle ->
        LaunchedEffect(index) {
            delay(sparkle.delayMs)
            while (true) {
                sparkle.scale.animateTo(1f, tween(sparkle.cycleDurationMs / 3))
                sparkle.scale.animateTo(0f, tween(sparkle.cycleDurationMs * 2 / 3))
                val (newX, newY) = randomEdgePosition()
                sparkle.xFraction.floatValue = newX
                sparkle.yFraction.floatValue = newY
                delay(sparkle.delayMs)
            }
        }
    }
    Box(modifier = modifier) {
        content()
        SparkleCanvas(
            modifier = Modifier.matchParentSize(),
            sparkles = sparkles,
            color = fontColor,
            density = density
        )
    }
}

@Composable
fun EdgeSparklesEffectWhen(isActive: Boolean, content: @Composable () -> Unit) {
    if (isActive) {
        EdgeSparklesEffect { content() }
    } else {
        content()
    }
}

private fun randomEdgePosition(): Pair<Float, Float> {
    val edge = Random.nextInt(4)
    val along = Random.nextFloat()
    return when (edge) {
        0 -> along to Random.nextFloat() * EDGE_THICKNESS
        1 -> along to 1f - Random.nextFloat() * EDGE_THICKNESS
        2 -> Random.nextFloat() * EDGE_THICKNESS to along
        else -> 1f - Random.nextFloat() * EDGE_THICKNESS to along
    }
}

private const val EDGE_THICKNESS = 0.3f
private const val SPARKLE_COUNT = 5
private const val SPARKLE_MIN_SIZE = 2f
private const val SPARKLE_SIZE_RANGE = 3f
private const val SPARKLE_STAGGER = 2000f
private const val SPARKLE_CYCLE_MIN = 1200
private const val SPARKLE_CYCLE_RANGE = 1000
private const val SPARKLE_ROTATION_RANGE = 0.8f
