package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.empty
import com.wojdor.memolki.shared.resources.leave_daily_challenge_body
import com.wojdor.memolki.shared.resources.leave_daily_challenge_title
import com.wojdor.memolki.shared.resources.leave_game_body
import com.wojdor.memolki.shared.resources.leave_game_leave
import com.wojdor.memolki.shared.resources.leave_game_stay
import com.wojdor.memolki.shared.resources.leave_game_title
import org.jetbrains.compose.resources.stringResource as composeStringResource
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.feature.game.GameCallbacks
import com.wojdor.memolki.ui.feature.game.GameState
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.FullRoundedShape
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXL
import com.wojdor.memolki.ui.theme.AppColors
import kotlin.math.ceil

@Composable
fun GameContent(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    CardsGridWithText(state, callbacks)
    if (state.shouldShowCardDetails) {
        CardDetails(state)
    }
    if (state.shouldShowLeaveConfirmation) {
        LeaveConfirmation(state.isDailyChallenge, callbacks)
    }
}

@Composable
private fun CardsGridWithText(
    state: GameState,
    callbacks: GameCallbacks
) {
    val animatedTextAlpha by animateFloatAsState(
        targetValue = if (state.shouldShowCardText) 1.0f else 0.0f,
        label = "on press card text animation",
        animationSpec = tween(durationMillis = CARD_TEXT_ANIMATION_DURATION)
    )
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacingL),
    ) {
        val spacing = spacingS
        val columns = state.board.columns
        if (columns > 0 && state.cards.isNotEmpty()) {
            val rows = ceil(state.cards.size / columns.toFloat()).toInt()
            val gridAreaHeight = maxHeight * GRID_AREA_RATIO
            val cardWidth = (maxWidth - spacing * (columns - 1)) / columns
            val cardHeight = (gridAreaHeight - spacing * (rows - 1)) / rows
            val cardSize = cardWidth.coerceAtMost(cardHeight)
            val actualGridHeight = cardSize * rows + spacing * (rows - 1)
            val topSpace = maxHeight * TOP_SPACE_RATIO
            val gridTopPadding = topSpace + (gridAreaHeight - actualGridHeight) / 2
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .height(gridTopPadding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (!state.isDailyChallenge) {
                        AutoSizeText(
                            text = stringResource(R.string.level_count, state.level),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    GameProgressBar(
                        progress = state.progress,
                        modifier = Modifier.padding(spacingL)
                    )
                }
                CardsGrid(
                    state = state,
                    callbacks = callbacks,
                    cardSize = cardSize,
                    columns = columns,
                    spacing = spacing
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AutoSizeText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(animatedTextAlpha),
                        text = composeStringResource(state.lastCardPressed.textRes),
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun CardDetails(state: GameState) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(contentAlignment = Alignment.Center) {
            val shorterEdge = maxWidth.coerceAtMost(maxHeight)
            Column(
                modifier = Modifier.padding(horizontal = spacingL)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                FrontCardItem(
                    modifier = Modifier
                        .size(shorterEdge)
                        .padding(spacingL),
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
                            .background(AppColors.Primary)
                            .padding(vertical = spacingL, horizontal = spacingXL),
                        text = composeStringResource(state.lastCardPressed.textRes),
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaveConfirmation(isDailyChallenge: Boolean, callbacks: GameCallbacks) {
    val titleRes =
        if (isDailyChallenge) Res.string.leave_daily_challenge_title else Res.string.leave_game_title
    val bodyRes =
        if (isDailyChallenge) Res.string.leave_daily_challenge_body else Res.string.leave_game_body
    Dialog(
        onDismissRequest = callbacks.onLeaveConfirmationDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = spacingXL)
                    .clip(CardShape)
                    .background(AppColors.Primary)
                    .padding(spacingXL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AutoSizeText(
                    text = composeStringResource(titleRes),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(spacingM))
                AutoSizeText(
                    text = composeStringResource(bodyRes),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(spacingXL))
                BaseMenuItem(
                    textId = Res.string.leave_game_stay,
                    onClick = callbacks.onLeaveConfirmationDismiss
                )
                Spacer(modifier = Modifier.height(spacingL))
                BaseMenuItem(
                    textId = Res.string.leave_game_leave,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    alpha = 0.5f,
                    onClick = callbacks.onLeaveConfirmationConfirm
                )
            }
        }
    }
}

private const val CARD_TEXT_ANIMATION_DURATION = 300
private const val TOP_SPACE_RATIO = 0.1f
private const val GRID_AREA_RATIO = 0.8f

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
                board = BoardModel.Grid2x3(),
                level = 5L,
                cards = List(6) {
                    CardModel.Text(
                        id = "id",
                        pairId = "pairId",
                        textRes = Res.string.empty
                    )
                }
            )
        )
    }
}

@Preview
@Composable
private fun CardsGridPartialProgressPreview() {
    AppTheme {
        GameContent(
            state = GameState(
                board = BoardModel.Grid2x3(),
                level = 12L,
                progress = 0.5f,
                cards = getPreviewCards(matchedCount = 3)
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
                board = BoardModel.Grid2x3(),
                level = 42L,
                progress = 1f,
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
                board = BoardModel.Grid2x3(),
                level = 7L,
                progress = 1f,
                cards = cards,
                lastCardPressed = cards.first(),
                shouldShowCardDetails = true
            )
        )
    }
}

@Preview
@Composable
private fun CardsGridDailyChallengePreview() {
    AppTheme {
        GameContent(
            state = GameState(
                board = BoardModel.Grid2x3(),
                isDailyChallenge = true,
                progress = 0.33f,
                cards = getPreviewCards(matchedCount = 2)
            )
        )
    }
}

@Preview
@Composable
private fun LeaveConfirmationDailyChallengePreview() {
    AppTheme {
        GameContent(
            state = GameState(
                board = BoardModel.Grid5x6(),
                isDailyChallenge = true,
                shouldShowLeaveConfirmation = true,
                progress = 0.33f,
                cards = getPreviewCards(matchedCount = 2)
            )
        )
    }
}

@Preview
@Composable
private fun LeaveConfirmationCasualPreview() {
    AppTheme {
        GameContent(
            state = GameState(
                board = BoardModel.Grid2x3(),
                shouldShowLeaveConfirmation = true,
                level = 5L,
                progress = 0.5f,
                cards = getPreviewCards(matchedCount = 3)
            )
        )
    }
}

private fun getPreviewCards(matchedCount: Int = 6) = List(6) {
    CardModel.Image(
        id = "id",
        pairId = "pairId",
        textRes = Res.string.empty,
        imageRes = if (it % 2 == 0) R.drawable.img_test_whole else R.drawable.img_test_half,
        isFlippedFront = true,
        isPairMatched = it < matchedCount
    )
}
