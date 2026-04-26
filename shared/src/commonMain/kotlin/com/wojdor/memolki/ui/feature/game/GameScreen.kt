package com.wojdor.memolki.ui.feature.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.app.navigateToEndGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.BackHandler
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.endgame.EndGameIntent
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.component.GameContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.gameservices.GameServices
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = koinViewModel(),
    endGameViewModel: EndGameViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, endGameViewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: GameViewModel,
    endGameViewModel: EndGameViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val gameServices = koinInject<GameServices>()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is GameEffect.OpenEndGameScreen -> openEndGameScreen(
                endGameViewModel,
                navController,
                effect
            )

            is GameEffect.SendTotalCardPairsMatchedScore -> coroutineScope.launch {
                gameServices.submitTotalCardPairsMatched(effect.totalCardPairsMatched)
            }

            is GameEffect.OnPairMatched -> viewModel.playMatchSound()
            is GameEffect.NavigateBack -> navController.popBackStack()
        }
    }
}

private fun openEndGameScreen(
    endGameViewModel: EndGameViewModel,
    navController: NavController,
    effect: GameEffect.OpenEndGameScreen
) {
    val dailyChallenge = effect.dailyChallenge
    if (dailyChallenge != DailyChallengeModel()) {
        endGameViewModel.sendIntent(
            EndGameIntent.OnDailyChallengeEndGameShow(
                boardModel = effect.boardModel,
                dailyChallengeModel = dailyChallenge
            )
        )
    } else {
        endGameViewModel.sendIntent(
            EndGameIntent.OnCasualEndGameShow(
                effect.boardModel,
                effect.level
            )
        )
    }
    navController.navigateToEndGame()
}

@Composable
private fun HandleState(
    viewModel: GameViewModel,
    state: GameState
) {
    DisposableEffect(Unit) {
        onDispose { viewModel.sendIntent(GameIntent.OnGameLeave) }
    }
    val callbacks = GameCallbacks(
        onBackCardClick = { viewModel.sendIntent(GameIntent.OnBackCardClick(it)) },
        onFrontCardPress = { isPressed, card ->
            viewModel.sendIntent(GameIntent.OnFrontCardPress(isPressed, card))
        },
        onMatchAnimationComplete = { viewModel.sendIntent(GameIntent.OnMatchAnimationComplete) },
        onMistakeShakeComplete = { viewModel.sendIntent(GameIntent.OnMistakeShakeComplete) },
        onBackPress = { viewModel.sendIntent(GameIntent.OnLeaveConfirmationShow) },
        onLeaveConfirmationDismiss = { viewModel.sendIntent(GameIntent.OnLeaveConfirmationDismiss) },
        onLeaveConfirmationConfirm = { viewModel.sendIntent(GameIntent.OnLeaveConfirmationConfirm) }
    )
    GameScreen(state, callbacks)
}

@Composable
private fun GameScreen(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    BackHandler(
        enabled = true,
        onBack = {
            if (!state.isGameFinished) {
                callbacks.onBackPress()
            }
        }
    )
    GameContent(
        state = state,
        callbacks = callbacks
    )
}

@Preview
@Composable
private fun GameScreenGrid2x3Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid2x3(),
                    cards = List(6) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenGrid3x4Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid3x4(),
                    cards = List(12) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenGrid4x4Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid4x4(),
                    cards = List(16) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenGrid4x5Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid4x5(),
                    cards = List(20) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenGrid4x6Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid4x6(),
                    cards = List(24) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenGrid5x6Preview() {
    AppTheme {
        PreviewBackground {
            GameScreen(
                state = GameState(
                    board = BoardModel.Grid5x6(),
                    cards = List(30) {
                        CardModel.Text(id = "id", pairId = "pairId", textRes = Res.string.empty)
                    }
                )
            )
        }
    }
}
