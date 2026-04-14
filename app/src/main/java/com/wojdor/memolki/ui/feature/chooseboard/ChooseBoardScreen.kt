package com.wojdor.memolki.ui.feature.chooseboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToDailyChallenge
import com.wojdor.memolki.ui.app.navigateToDailyChallengeHistory
import com.wojdor.memolki.ui.app.navigateToGame
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.chooseboard.component.ChooseBoardItem
import com.wojdor.memolki.ui.feature.chooseboard.component.DailyChallengeItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXL
import com.wojdor.memolki.ui.theme.spacingXXXL

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
            ChooseBoardEffect.OpenCollectionScreen -> navController.navigateToCollection()
            ChooseBoardEffect.OpenDailyChallengeHistoryScreen -> navController.navigateToDailyChallengeHistory()
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
        onDailyChallengeClick = { viewModel.sendIntent(ChooseBoardIntent.OnDailyChallengeClick) },
        onLockedBoardClick = { viewModel.sendIntent(ChooseBoardIntent.OnLockedBoardClick) },
        onDailyChallengeHistoryClick = { viewModel.sendIntent(ChooseBoardIntent.OnDailyChallengeHistoryClick) }
    )
    ChooseBoardScreen(state, callbacks)
}

@Composable
private fun ChooseBoardScreen(
    state: ChooseBoardState,
    callbacks: ChooseBoardCallbacks
) {
    val (unlockedBoards, lockedBoards) = state.boards.partition { it.isUnlocked }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        unlockedBoards.forEach { board ->
            ChooseBoardItem(
                textId = board.textId,
                isEnabled = true,
                onClick = { callbacks.onBoardClick(board) }
            )
            Spacer(modifier = Modifier.height(spacingXL))
        }
        DailyChallengeItem(
            isCompleted = state.isDailyChallengeCompleted,
            showHistoryIcon = state.hasDailyChallengeHistory,
            onClick = { callbacks.onDailyChallengeClick() },
            onHistoryClick = { callbacks.onDailyChallengeHistoryClick() }
        )
        if (lockedBoards.isNotEmpty()) {
            Spacer(modifier = Modifier.height(spacingXXXL))
            Text(
                text = stringResource(R.string.need_more_cards).lowercase(),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(spacingS))
        }
        lockedBoards.forEachIndexed { index, board ->
            ChooseBoardItem(
                textId = board.textId,
                isEnabled = false,
                onClick = { callbacks.onBoardClick(board) },
                onLockedClick = { callbacks.onLockedBoardClick() }
            )
            if (index != lockedBoards.lastIndex) {
                Spacer(modifier = Modifier.height(spacingXL))
            }
        }
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
                isDailyChallengeCompleted = true,
                hasDailyChallengeHistory = true
            ),
            callbacks = ChooseBoardCallbacks()
        )
    }
}
