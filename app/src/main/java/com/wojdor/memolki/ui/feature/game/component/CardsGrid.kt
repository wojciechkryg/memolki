package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.feature.game.GameState
import kotlin.math.min

@Composable
fun CardsGrid(state: GameState) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val shorterEdge = maxWidth.coerceAtMost(maxHeight)
        val padding = 4.dp
        val columns = state.level.columns
        val rows = state.level.rows
        val gridSize = min(columns, rows) + 1
        val cardSize = (shorterEdge - padding * gridSize) / gridSize

        Column {
            repeat(rows) {
                Row {
                    repeat(columns) {
                        BackCardItem(
                            modifier = Modifier
                                .padding(padding)
                                .size(cardSize)
                        )
                    }
                }
            }
        }
    }
}
