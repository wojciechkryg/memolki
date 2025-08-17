package com.wojdor.memolki.ui.feature.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.ui.base.CollectUiEffects

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
    Box(modifier = Modifier.fillMaxSize())
    {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(state.level.textId)
        )
    }
}
