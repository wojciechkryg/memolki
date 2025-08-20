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
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.feature.game.GameState
import com.wojdor.memolki.ui.theme.AppTheme
import kotlin.math.min

@Composable
fun GameCardsGrid(state: GameState) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val shorterEdge = maxWidth.coerceAtMost(maxHeight)
        val spacing = 8.dp
        val columns = state.level.columns
        val rows = state.level.rows
        val cellsOnShortEdge = min(columns, rows)
        val cardSize = (shorterEdge - spacing * cellsOnShortEdge) / cellsOnShortEdge

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            repeat(rows) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    repeat(columns) {
                        BackCardItem(
                            modifier = Modifier.size(cardSize)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CardsGridPreview() {
    AppTheme {
        GameCardsGrid(
            state = GameState(
                LevelModel.Grid2x3
            )
        )
    }
}
