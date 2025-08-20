package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    card: CardModel,
    callbacks: GameCallbacks
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = CARD_FLIP_DURATION),
        label = "cardFlip"
    )

    val scale = 1.0f - SCALE_DOWN_FACTOR * sin(rotation * PI / 180).toFloat()

    if (rotation < 90f) {
        BackCardItem(
            modifier = modifier.graphicsLayer {
                rotationY = rotation
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            },
            onClick = { callbacks.onCardClick(card) }
        )
    } else {
        FrontCardItem(
            modifier = modifier.graphicsLayer {
                rotationY = rotation - 180f
                cameraDistance = CAMERA_DISTANCE_FACTOR * density
                scaleX = scale
                scaleY = scale
            },
            card = card
        )
    }
}

private const val CARD_FLIP_DURATION = 500
private const val CAMERA_DISTANCE_FACTOR = 12
private const val SCALE_DOWN_FACTOR = 0.2f
