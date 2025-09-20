package com.wojdor.memolki.ui.feature.endgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.component.EndGameContent
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun EndGameScreen(
    viewModel: EndGameViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, gameViewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: EndGameViewModel,
    gameViewModel: GameViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        when (it) {
            is EndGameEffect.OpenGameScreen -> openGameScreen(
                gameViewModel,
                navController,
                it.levelModel
            )

            is EndGameEffect.OpenMenuScreen -> navController.navigateToMenu()
        }
    }
}

private fun openGameScreen(
    gameViewModel: GameViewModel,
    navController: NavController,
    level: LevelModel
) {
    gameViewModel.sendIntent(GameIntent.OnLevelStart(level))
    navController.navigateToGameFromEndGame()
}

@Composable
private fun HandleState(
    viewModel: EndGameViewModel,
    state: EndGameState
) {
    val callbacks = EndGameCallbacks(
        onPlayAgainClick = { viewModel.sendIntent(EndGameIntent.OnPlayAgainClick(state.level)) },
        onMenuClick = { viewModel.sendIntent(EndGameIntent.OnMenuClick) }
    )
    EndGameScreen(state, callbacks)
}

@Composable
fun EndGameScreen(
    state: EndGameState,
    callbacks: EndGameCallbacks = EndGameCallbacks()
) {
    EndGameContent(state, callbacks)
}

@Composable
@Preview(showBackground = true)
private fun EndGameScreenPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu)
            )
        )
    }
}
