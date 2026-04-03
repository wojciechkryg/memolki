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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.app.navigateToDailyChallenge
import com.wojdor.memolki.ui.app.navigateToGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.chooselevel.component.ChooseLevelItem
import com.wojdor.memolki.ui.feature.chooselevel.component.DailyChallengeItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingXL

@Composable
fun ChooseLevelScreen(
    viewModel: ChooseLevelViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ChooseLevelViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        when (it) {
            is ChooseLevelEffect.OpenGameScreen -> navController.navigateToGame(it.levelModel.id)
            ChooseLevelEffect.OpenDailyChallengeScreen -> navController.navigateToDailyChallenge()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: ChooseLevelViewModel,
    state: ChooseLevelState
) {
    val callbacks = ChooseLevelCallbacks(
        onLevelClick = { viewModel.sendIntent(ChooseLevelIntent.OnLevelClick(it)) },
        onDailyChallengeClick = { viewModel.sendIntent(ChooseLevelIntent.OnDailyChallengeClick) }
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
            Spacer(modifier = Modifier.height(spacingXL))
            ChooseLevelItem(
                textId = level.textId,
                isEnabled = level.isUnlocked,
                onClick = { callbacks.onLevelClick(level) }
            )
        }
        Spacer(modifier = Modifier.height(spacingXL))
        DailyChallengeItem(
            isCompleted = state.isDailyChallengeCompleted,
            onClick = { callbacks.onDailyChallengeClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseLevelScreenPreview() {
    AppTheme {
        ChooseLevelScreen(
            state = ChooseLevelState(
                levels = listOf(
                    LevelModel.Grid2x3(isUnlocked = true),
                    LevelModel.Grid3x4(isUnlocked = true),
                    LevelModel.Grid4x4(),
                    LevelModel.Grid4x5(),
                    LevelModel.Grid4x6(),
                    LevelModel.Grid5x6()
                )
            ),
            callbacks = ChooseLevelCallbacks()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseLevelScreenDailyChallengeCompletedPreview() {
    AppTheme {
        ChooseLevelScreen(
            state = ChooseLevelState(
                levels = listOf(
                    LevelModel.Grid2x3(isUnlocked = true),
                    LevelModel.Grid3x4(isUnlocked = true),
                    LevelModel.Grid4x4(),
                    LevelModel.Grid4x5(),
                    LevelModel.Grid4x6(),
                    LevelModel.Grid5x6()
                ),
                isDailyChallengeCompleted = true
            ),
            callbacks = ChooseLevelCallbacks()
        )
    }
}
