package com.wojdor.memolki.ui.feature.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToEndGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.EndGameIntent
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.component.GameCardsGrid
import com.wojdor.memolki.ui.theme.AppTheme

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
    CollectUiEffects(viewModel) {
        when (it) {
            is GameEffect.OpenEndGameScreen -> openEndGameScreen(
                endGameViewModel,
                navController,
                it.levelModel
            )
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

@Composable
private fun HandleState(
    viewModel: GameViewModel,
    state: GameState
) {
    val callbacks = GameCallbacks(
        onBackCardClick = { viewModel.sendIntent(GameIntent.OnBackCardClick(it)) }
    )
    GameScreen(state, callbacks)
}

@Composable
fun GameScreen(
    state: GameState,
    callbacks: GameCallbacks = GameCallbacks()
) {
    GameCardsGrid(
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid3x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid3x4,
                cards = List(4) {
                    List(3) {
                        CardModel.Text(
                            id = "id",
                            pairId = "pairId",
                            textRes = R.string.empty
                        )
                    }
                })
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid4x4,
                cards = List(4) {
                    List(4) {
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

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x5Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid4x5,
                cards = List(5) {
                    List(4) {
                        CardModel.Text(
                            id = "id",
                            pairId = "pairId",
                            textRes = R.string.empty
                        )
                    }
                })
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid5x6Preview() {
    AppTheme {
        GameScreen(
            state = GameState(
                level = LevelModel.Grid5x6,
                cards = List(6) {
                    List(5) {
                        CardModel.Text(
                            id = "id",
                            pairId = "pairId",
                            textRes = R.string.empty
                        )
                    }
                })
        )
    }
}
