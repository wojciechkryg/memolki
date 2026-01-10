package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState

@Composable
fun CardsGrid(
    modifier: Modifier = Modifier,
    state: GameState,
    callbacks: GameCallbacks,
    cardSize: Dp,
    columns: Int,
    spacing: Dp
) {
    // LazyVerticalGrid doesn't work well on tablets with smaller card sizes
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.cards.chunked(columns).forEach { rowCards ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally)
            ) {
                rowCards.forEach { card ->
                    FlippableCardItem(
                        modifier = Modifier.size(cardSize),
                        card = card,
                        callbacks = callbacks
                    )
                }
            }
        }
    }
}
