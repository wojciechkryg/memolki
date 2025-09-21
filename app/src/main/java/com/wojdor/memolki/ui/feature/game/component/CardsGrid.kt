package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun GameCardsGrid(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val spacing = 8.dp
        val columns = state.level.columns
        if (columns > 0) {
            val shorterEdge = maxWidth.coerceAtMost(maxHeight)
            val cardSize = (shorterEdge - spacing * (columns - 1)) / columns
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
                    AnimatedCardItem(
                        modifier = Modifier.size(cardSize),
                        card = card,
                        callbacks = callbacks
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EmptyCardsGridPreview() {
    AppTheme {
        GameCardsGrid(
            state = GameState(),
        )
    }
}

@Preview
@Composable
private fun CardsGridPreview() {
    AppTheme {
        GameCardsGrid(
            state = GameState(
                level = LevelModel.Grid2x3(),
                cards = List(6) {
                    CardModel.Text(
                        id = "id",
                        pairId = "pairId",
                        textRes = R.string.empty
                    )
                }
            )
        )
    }
}
