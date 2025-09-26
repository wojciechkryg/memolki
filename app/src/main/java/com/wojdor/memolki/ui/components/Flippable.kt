package com.wojdor.memolki.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun Flippable(
    modifier: Modifier = Modifier,
    isFlipped: Boolean,
    frontSide: @Composable (modifier: Modifier) -> Unit,
    backSide: @Composable (modifier: Modifier) -> Unit,
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isFlipped) 1f else 0f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = "flip animation"
    )

    val rotationY = animationProgress * 180f
    val rotationZ = (-(animationProgress * 2 - 1).pow(2) + 1) * ROTATION_FACTOR
    val scale = 1.0f - SCALE_DOWN_FACTOR * sin(animationProgress * PI).toFloat()

    if (rotationY < 90f) {
        backSide(
            modifier.graphicsLayer {
                this.rotationY = rotationY
                this.rotationZ = rotationZ
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            }
        )
    } else {
        frontSide(
            modifier.graphicsLayer {
                this.rotationY = rotationY - 180f
                this.rotationZ = rotationZ
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            }
        )
    }
}

private const val SCALE_DOWN_FACTOR = 0.2f
private const val ROTATION_FACTOR = 10
private const val CAMERA_DISTANCE_FACTOR = 12
private const val ANIMATION_DURATION = 400
