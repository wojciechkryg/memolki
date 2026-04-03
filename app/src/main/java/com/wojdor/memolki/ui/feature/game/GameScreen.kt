package com.wojdor.memolki.ui.feature.game

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
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
    val coroutineScope = rememberCoroutineScope()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is GameEffect.OpenEndGameScreen -> openEndGameScreen(
                endGameViewModel,
                navController,
                effect
            )

            is GameEffect.SendTotalCardPairsMatchedScore -> activity?.let {
                coroutineScope.launch {
                    submitTotalCardPairsMatched(
                        it,
                        effect.googlePlayGames,
                        effect.totalCardPairsMatched
                    )
                }
            }
        }
    }
}

private suspend fun submitTotalCardPairsMatched(
    activity: Activity,
    googlePlayGames: GooglePlayGames,
    totalCardPairsMatched: Long
) {
    googlePlayGames.submitTotalCardPairsMatched(activity, totalCardPairsMatched)
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
                levelModel = effect.levelModel,
                dailyChallengeModel = dailyChallenge
            )
        )
    } else {
        endGameViewModel.sendIntent(EndGameIntent.OnCasualEndGameShow(effect.levelModel))
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
        onMistakeShakeComplete = { viewModel.sendIntent(GameIntent.OnMistakeShakeComplete) }
    )
    GameScreen(state, callbacks)
}

@Composable
private fun GameScreen(
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
