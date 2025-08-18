package com.wojdor.memolki.ui.feature.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.game.component.CardsGrid
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: GameViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        // TODO: Handle game effects here if needed
    }
}

@Composable
private fun HandleState(
    viewModel: GameViewModel,
    state: GameState
) {
    val callbacks = GameCallbacks()
    GameScreen(state, callbacks)
}

@Composable
fun GameScreen(
    state: GameState,
    callbacks: GameCallbacks
) {
    CardsGrid(state = state)
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid2x3Preview() {
    AppTheme {
        GameScreen(
            state = GameState(LevelModel.Grid2x3),
            callbacks = GameCallbacks()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid3x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(LevelModel.Grid3x4),
            callbacks = GameCallbacks()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x4Preview() {
    AppTheme {
        GameScreen(
            state = GameState(LevelModel.Grid4x4),
            callbacks = GameCallbacks()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid4x5Preview() {
    AppTheme {
        GameScreen(
            state = GameState(LevelModel.Grid4x5),
            callbacks = GameCallbacks()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun GameScreenGrid5x6Preview() {
    AppTheme {
        GameScreen(
            state = GameState(LevelModel.Grid5x6),
            callbacks = GameCallbacks()
        )
    }
}
