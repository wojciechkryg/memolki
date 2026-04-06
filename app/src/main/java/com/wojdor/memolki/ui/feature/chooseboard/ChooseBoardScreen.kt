package com.wojdor.memolki.ui.feature.chooseboard

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
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.app.navigateToDailyChallenge
import com.wojdor.memolki.ui.app.navigateToGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.chooseboard.component.ChooseBoardItem
import com.wojdor.memolki.ui.feature.chooseboard.component.DailyChallengeItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingXL

@Composable
fun ChooseBoardScreen(
    viewModel: ChooseBoardViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ChooseBoardViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) {
        when (it) {
            is ChooseBoardEffect.OpenGameScreen -> navController.navigateToGame(it.boardModel.id)
            ChooseBoardEffect.OpenDailyChallengeScreen -> navController.navigateToDailyChallenge()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: ChooseBoardViewModel,
    state: ChooseBoardState
) {
    val callbacks = ChooseBoardCallbacks(
        onBoardClick = { viewModel.sendIntent(ChooseBoardIntent.OnBoardClick(it)) },
        onDailyChallengeClick = { viewModel.sendIntent(ChooseBoardIntent.OnDailyChallengeClick) }
    )
    ChooseBoardScreen(state, callbacks)
}

@Composable
private fun ChooseBoardScreen(
    state: ChooseBoardState,
    callbacks: ChooseBoardCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.boards.forEach { board ->
            Spacer(modifier = Modifier.height(spacingXL))
            ChooseBoardItem(
                textId = board.textId,
                isEnabled = board.isUnlocked,
                onClick = { callbacks.onBoardClick(board) }
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
private fun ChooseBoardScreenPreview() {
    AppTheme {
        ChooseBoardScreen(
            state = ChooseBoardState(
                boards = listOf(
                    BoardModel.Grid2x3(isUnlocked = true),
                    BoardModel.Grid3x4(isUnlocked = true),
                    BoardModel.Grid4x4(),
                    BoardModel.Grid4x5(),
                    BoardModel.Grid4x6(),
                    BoardModel.Grid5x6()
                )
            ),
            callbacks = ChooseBoardCallbacks()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseBoardScreenDailyChallengeCompletedPreview() {
    AppTheme {
        ChooseBoardScreen(
            state = ChooseBoardState(
                boards = listOf(
                    BoardModel.Grid2x3(isUnlocked = true),
                    BoardModel.Grid3x4(isUnlocked = true),
                    BoardModel.Grid4x4(),
                    BoardModel.Grid4x5(),
                    BoardModel.Grid4x6(),
                    BoardModel.Grid5x6()
                ),
                isDailyChallengeCompleted = true
            ),
            callbacks = ChooseBoardCallbacks()
        )
    }
}
