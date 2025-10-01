package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.component.Flippable
import com.wojdor.memolki.ui.feature.game.GameCallbacks

@Composable
fun FlippableCardItem(
    modifier: Modifier = Modifier,
    card: CardModel,
    callbacks: GameCallbacks
) {
    Flippable(
        modifier = modifier,
        isFlipped = card.isFlippedFront || card.isPairMatched,
        frontSide = { modifier ->
            FrontCardItem(
                modifier = modifier,
                card = card,
                onPress = { callbacks.onFrontCardPress(it, card) }
            )
        },
        backSide = { modifier ->
            BackCardItem(
                modifier = modifier,
                onClick = { callbacks.onBackCardClick(card) }
            )
        }
    )
}
