package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState

@Composable
fun CardsGrid(
    columns: Int,
    spacing: Dp,
    state: GameState,
    cardSize: Dp,
    callbacks: GameCallbacks
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        userScrollEnabled = false,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        items(state.cards) { card ->
            FlippableCardItem(
                modifier = Modifier.size(cardSize),
                card = card,
                callbacks = callbacks
            )
        }
    }
}
