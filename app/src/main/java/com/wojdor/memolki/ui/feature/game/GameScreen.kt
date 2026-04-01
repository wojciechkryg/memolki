package com.wojdor.memolki.ui.feature.game

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToEndGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.EndGameIntent
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.component.GameContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.playgames.GooglePlayGames
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    endGameViewModel: EndGameViewModel = hiltViewModel(),
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
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is GameEffect.OpenEndGameScreen -> openEndGameScreen(
                endGameViewModel,
                navController,
                effect.levelModel
            )

            is GameEffect.SendTotalCardPairsMatchedScore -> activity?.let {
                sendTotalCardPairsMatchedScore(
                    it,
                    viewModel,
                    effect.googlePlayGames,
                    effect.totalCardPairsMatched
                )
            }
        }
    }
}

private fun openEndGameScreen(
    endGameViewModel: EndGameViewModel,
    navController: NavController,
    level: LevelModel
) {
    endGameViewModel.sendIntent(EndGameIntent.OnEndGameShow(level))
    navController.navigateToEndGame()
}

private fun sendTotalCardPairsMatchedScore(
    activity: Activity,
    viewModel: GameViewModel,
    googlePlayGames: GooglePlayGames,
    totalCardPairsMatched: Long
) {
    viewModel.viewModelScope.launch {
        googlePlayGames.submitTotalCardPairsMatched(activity, totalCardPairsMatched)
    }
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
        onMismatchShakeComplete = { viewModel.sendIntent(GameIntent.OnMismatchShakeComplete) }
    )
    GameScreen(state, callbacks)
}

@Composable
fun GameScreen(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    BackHandler(enabled = state.isGameFinished, onBack = {})
    GameContent(
        state = state,
        callbacks = callbacks
    )
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid2x3Preview() {
    AppTheme {
        GameScreen(
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid3x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid3x4(),
                cards = List(12) {
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid4x4(),
                cards = List(16) {
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x5Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid4x5(),
                cards = List(20) {
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x6Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid4x6(),
                cards = List(24) {
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid5x6Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid5x6(),
                cards = List(30) {
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
