package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlin.math.min

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
        val shorterEdge = maxWidth.coerceAtMost(maxHeight)
        val spacing = 8.dp
        val rows = state.cards.size
        val columns = state.cards.firstOrNull()?.size ?: 0
        val cellsOnShortEdge = min(columns, rows)
        val cardSize = (shorterEdge - spacing * cellsOnShortEdge) / cellsOnShortEdge
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            repeat(columns) { column ->
                Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                    repeat(rows) { row ->
                        AnimatedCardItem(
                            modifier = Modifier.size(cardSize),
                            card = state.cards[row][column],
                            callbacks = callbacks
                        )
                    }
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
                level = LevelModel.Grid2x3,
                cards = List(3) {
                    List(2) {
                        CardModel.Text(
                            id = "id",
                            pairId = "pairId",
                            textRes = R.string.empty
                        )
                    }
                }
            )
        )
    }
}
