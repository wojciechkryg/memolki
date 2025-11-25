package com.wojdor.memolki.ui.feature.endgame

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.component.EndGameContent
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.theme.AppTheme
import kotlinx.coroutines.launch

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
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is EndGameEffect.OpenGameScreen -> openGameScreen(
                gameViewModel,
                navController,
                effect.levelModel
            )

            EndGameEffect.OpenMenuScreen -> navController.navigateToMenu()
            EndGameEffect.OpenCollectionScreen -> navController.navigateToCollection()
            is EndGameEffect.ShowAd -> activity?.let { showAd(it, viewModel, effect.rewardedAd) }
            is EndGameEffect.RequestReview -> activity?.let {
                launchReviewFlow(
                    it,
                    effect.reviewManager,
                    effect.reviewInfo
                )
            }

            is EndGameEffect.SendTotalCoinsScore -> activity?.let {
                sendTotalCoinsScore(
                    it,
                    viewModel,
                    effect.googlePlayGames,
                    effect.totalCoins
                )
            }
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

private fun showAd(
    activity: Activity,
    viewModel: EndGameViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        activity,
        onGrantReward = { viewModel.sendIntent(EndGameIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(EndGameIntent.OnAdDismiss(it)) }
    )
}

private fun launchReviewFlow(
    activity: Activity,
    reviewManager: ReviewManager,
    reviewInfo: ReviewInfo
) {
    reviewManager.launchReviewFlow(activity, reviewInfo)
}

private fun sendTotalCoinsScore(
    activity: Activity,
    viewModel: EndGameViewModel,
    googlePlayGames: GooglePlayGames,
    totalCoins: Long
) {
    viewModel.viewModelScope.launch {
        googlePlayGames.submitTotalCoins(activity, totalCoins)
    }
}

@Composable
private fun HandleState(
    viewModel: EndGameViewModel,
    state: EndGameState
) {
    val callbacks = EndGameCallbacks(
        onPlayAgainClick = { viewModel.sendIntent(EndGameIntent.OnPlayAgainClick(state.level)) },
        onMenuClick = { viewModel.sendIntent(EndGameIntent.OnMenuClick) },
        onUnlockNewCardClick = { viewModel.sendIntent(EndGameIntent.OnUnlockNewCardClick) },
        onWatchAdClick = { viewModel.sendIntent(EndGameIntent.OnWatchAdClick) }
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
                menu = listOf(
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu
                )
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EndGameScreenWithAdPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.WatchAd,
                    EndGameMenuModel.UnlockNewCard,
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu
                ),
            )
        )
    }
}
