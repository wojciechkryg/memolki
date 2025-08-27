package com.wojdor.memolki.ui.feature.endgame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.component.CoinsReward
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun EndGameScreen(
    viewModel: EndGameViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: EndGameViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        // TODO: Handle game effects here if needed
    }
}

@Composable
private fun HandleState(
    viewModel: EndGameViewModel,
    state: EndGameState
) {
    val callbacks = EndGameCallbacks()
    EndGameScreen(state, callbacks)
}

@Composable
fun EndGameScreen(
    state: EndGameState,
    callbacks: EndGameCallbacks = EndGameCallbacks()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CoinsReward(Modifier.align(Alignment.Center), state)
    }
}

@Composable
@Preview(showBackground = true)
private fun EndGamePreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(level = LevelModel.Grid2x3, rewardedCoins = 1234)
        )
    }
}
