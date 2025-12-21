package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun GameContent(
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
            val animatedAlpha by animateFloatAsState(
                targetValue = if (state.shouldShowCardText) 1.0f else 0.0f,
                label = "on press card label animation",
                animationSpec = tween(durationMillis = CARD_LABEL_ANIMATION_DURATION)
            )
            Column {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(animatedAlpha)
                )
                CardsGrid(columns, spacing, state, cardSize, callbacks)
                AutoSizeText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .alpha(animatedAlpha),
                    text = stringResource(state.lastCardPressed.textRes),
                    style = MaterialTheme.typography.displayMedium,
                )
            }
        }
    }
}

private const val CARD_LABEL_ANIMATION_DURATION = 300

@Preview
@Composable
private fun EmptyCardsGridPreview() {
    AppTheme {
        GameContent(
            state = GameState(),
        )
    }
}

@Preview
@Composable
private fun CardsGridPreview() {
    AppTheme {
        GameContent(
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
