package com.wojdor.memolki.ui.feature.endgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.component.CoinsReward
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.feature.menu.component.MenuItem
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoinsReward(state = state)
        Spacer(modifier = Modifier.height(64.dp))
        state.menu.forEach { menuItem ->
            Spacer(modifier = Modifier.height(16.dp))
            when (menuItem) {
                is EndGameMenuModel.PlayAgain -> MenuItem(
                    textId = menuItem.textId,
                    onClick = callbacks.onPlayAgainClick
                )

                is EndGameMenuModel.Menu -> MenuItem(
                    textId = menuItem.textId,
                    onClick = callbacks.onMenuClick
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EndGamePreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu)
            )
        )
    }
}
