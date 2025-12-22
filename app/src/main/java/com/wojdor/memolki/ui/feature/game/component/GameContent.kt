package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.FullRoundedShape

@Composable
fun GameContent(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    CardsGridWithText(state, callbacks)
    CardDetails(state)
}

@Composable
private fun CardsGridWithText(
    state: GameState,
    callbacks: GameCallbacks
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val spacing = 8.dp
        val columns = state.level.columns
        if (columns > 0) {
            val shorterEdge = maxWidth.coerceAtMost(maxHeight)
            val cardSize = (shorterEdge - spacing * (columns - 1)) / columns
            val animatedTextAlpha by animateFloatAsState(
                targetValue = if (state.shouldShowCardText) 1.0f else 0.0f,
                label = "on press card text animation",
                animationSpec = tween(durationMillis = CARD_TEXT_ANIMATION_DURATION)
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(animatedTextAlpha)
                )
                CardsGrid(columns, spacing, state, cardSize, callbacks)
                AutoSizeText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .alpha(animatedTextAlpha),
                    text = stringResource(state.lastCardPressed.textRes),
                    style = MaterialTheme.typography.displayMedium,
                )
            }
        }
    }
}

@Composable
private fun CardDetails(state: GameState) {
    var showDialog by remember { mutableStateOf(state.shouldShowCardImage) }
    LaunchedEffect(state.shouldShowCardImage) {
        showDialog = state.shouldShowCardImage
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                val shorterEdge = maxWidth.coerceAtMost(maxHeight)
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    FrontCardItem(
                        modifier = Modifier
                            .size(shorterEdge)
                            .padding(24.dp),
                        card = state.lastCardPressed
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,

                        ) {
                        AutoSizeText(
                            modifier = Modifier
                                .clip(FullRoundedShape)
                                .background(colorResource(R.color.primary))
                                .padding(vertical = 16.dp, horizontal = 32.dp),
                            text = stringResource(state.lastCardPressed.textRes),
                            style = MaterialTheme.typography.displayMedium,
                        )
                    }
                }
            }
        }
    }
}

private const val CARD_TEXT_ANIMATION_DURATION = 300

@Preview
@Composable
private fun EmptyGameContentPreview() {
    AppTheme {
        GameContent(
            state = GameState(),
        )
    }
}

@Preview
@Composable
private fun StartGameContentPreview() {
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

@Preview
@Composable
private fun CardsGridPreview() {
    val cards = getPreviewCards()
    AppTheme {
        GameContent(
            state = GameState(
                level = LevelModel.Grid2x3(),
                cards = cards,
                lastCardPressed = cards.first(),
                shouldShowCardText = true
            )
        )
    }
}

@Preview
@Composable
private fun CardsGridPressedPreview() {
    val cards = getPreviewCards()
    AppTheme {
        GameContent(
            state = GameState(
                level = LevelModel.Grid2x3(),
                cards = cards,
                lastCardPressed = cards.first(),
                shouldShowCardImage = true
            )
        )
    }
}

private fun getPreviewCards() = List(6) {
    CardModel.Image(
        id = "id",
        pairId = "pairId",
        textRes = R.string.empty,
        imageRes = if (it % 2 == 0) R.drawable.img_test_whole else R.drawable.img_test_half,
        isFlippedFront = true,
        isPairMatched = true
    )
}
