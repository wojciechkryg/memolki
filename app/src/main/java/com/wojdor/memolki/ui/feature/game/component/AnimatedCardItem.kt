package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun AnimatedCardItem(
    modifier: Modifier = Modifier,
    card: CardModel,
    callbacks: GameCallbacks
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (card.isFlippedFront) 1f else 0f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = "cardFlip"
    )

    val rotationY = animationProgress * 180f
    val rotationZ = (-(animationProgress * 2 - 1).pow(2) + 1) * ROTATION_FACTOR
    val scale = 1.0f - SCALE_DOWN_FACTOR * sin(animationProgress * PI).toFloat()

    if (rotationY < 90f) {
        BackCardItem(
            modifier = modifier.graphicsLayer {
                this.rotationY = rotationY
                this.rotationZ = rotationZ
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            },
            onClick = { callbacks.onBackCardClick(card) }
        )
    } else {
        FrontCardItem(
            modifier = modifier.graphicsLayer {
                this.rotationY = rotationY - 180f
                this.rotationZ = rotationZ
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            },
            card = card
        )
    }
}

private const val SCALE_DOWN_FACTOR = 0.2f
private const val ROTATION_FACTOR = 10
private const val CAMERA_DISTANCE_FACTOR = 12
private const val ANIMATION_DURATION = 400
