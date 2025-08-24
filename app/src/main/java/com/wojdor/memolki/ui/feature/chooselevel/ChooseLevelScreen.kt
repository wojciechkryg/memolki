package com.wojdor.memolki.ui.feature.chooselevel

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
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.chooselevel.component.ChooseLevelItem
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ChooseLevelScreen(
    viewModel: ChooseLevelViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, gameViewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ChooseLevelViewModel,
    gameViewModel: GameViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        when (it) {
            is ChooseLevelEffect.OpenGameScreen -> openGameScreen(
                gameViewModel,
                navController,
                it.levelModel
            )
        }
    }
}

private fun openGameScreen(
    gameViewModel: GameViewModel,
    navController: NavController,
    level: LevelModel
) {
    gameViewModel.sendIntent(GameIntent.OnLevelStart(level))
    navController.navigateToGame()
}

@Composable
private fun HandleState(
    viewModel: ChooseLevelViewModel,
    state: ChooseLevelState
) {
    val callbacks = ChooseLevelCallbacks(
        onLevelClick = { viewModel.sendIntent(ChooseLevelIntent.OnLevelClick(it)) }
    )
    ChooseLevelScreen(state, callbacks)
}

@Composable
private fun ChooseLevelScreen(
    state: ChooseLevelState,
    callbacks: ChooseLevelCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.levels.forEach { level ->
            Spacer(modifier = Modifier.height(24.dp))
            ChooseLevelItem(
                textId = level.textId,
                onClick = { callbacks.onLevelClick(level) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseLevelScreenPreview() {
    AppTheme {
        ChooseLevelScreen(
            state = ChooseLevelState(
                levels = listOf(
                    LevelModel.Grid2x3,
                    LevelModel.Grid3x4,
                    LevelModel.Grid4x4,
                    LevelModel.Grid4x5,
                    LevelModel.Grid5x6
                )
            ),
            callbacks = ChooseLevelCallbacks()
        )
    }
}
