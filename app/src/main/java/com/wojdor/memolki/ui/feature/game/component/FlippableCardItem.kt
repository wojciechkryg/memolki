package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.component.Flippable
import com.wojdor.memolki.ui.component.rememberShakeOffset
import com.wojdor.memolki.ui.feature.game.GameCallbacks

@Composable
fun FlippableCardItem(
    modifier: Modifier = Modifier,
    card: CardModel,
    callbacks: GameCallbacks
) {
    val matchScale = rememberMatchBounceScale(
        isAnimating = card.isMatchAnimating,
        onComplete = callbacks.onMatchAnimationComplete
    )
    val shakeOffset = rememberShakeOffset(
        isShaking = card.isMismatchShaking,
        delayMs = MISMATCH_VIEW_DELAY,
        onComplete = callbacks.onMismatchShakeComplete
    )
    Flippable(
        modifier = modifier.graphicsLayer {
            scaleX = matchScale
            scaleY = matchScale
            translationX = shakeOffset
        },
        isFlipped = card.isFlippedFront || card.isPairMatched,
        frontSide = { flipModifier ->
            FrontCardItem(
                modifier = flipModifier,
                card = card,
                onPress = { callbacks.onFrontCardPress(it, card) }
            )
        },
        backSide = { flipModifier ->
            BackCardItem(
                modifier = flipModifier,
                onClick = { callbacks.onBackCardClick(card) }
            )
        }
    )
}

private const val MISMATCH_VIEW_DELAY = 1700L
