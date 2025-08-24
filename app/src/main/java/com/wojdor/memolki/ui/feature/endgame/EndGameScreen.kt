package com.wojdor.memolki.ui.feature.endgame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.ui.base.CollectUiEffects

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
    FinishLevelScreen(state, callbacks)
}

@Composable
fun FinishLevelScreen(
    state: EndGameState,
    callbacks: EndGameCallbacks = EndGameCallbacks()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Finish level"
        )
    }
}
