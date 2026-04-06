package com.wojdor.memolki.ui.feature.endgame

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToEnableNotifications
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.endgame.component.CasualEndGameContent
import com.wojdor.memolki.ui.feature.endgame.component.DailyChallengeEndGameContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.playgames.GooglePlayGames
import kotlinx.coroutines.launch

@Composable
fun EndGameScreen(
    viewModel: EndGameViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    LifecycleResumeEffect(Unit) {
        viewModel.sendIntent(EndGameIntent.OnScreenResume)
        onPauseOrDispose {}
    }
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: EndGameViewModel,
    navController: NavController
) {
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is EndGameEffect.OpenGameScreen -> openGameScreen(navController, effect.boardModel)
            EndGameEffect.OpenMenuScreen -> navController.navigateToMenu()
            EndGameEffect.OpenCollectionScreen -> navController.navigateToCollection()
            is EndGameEffect.OpenEnableNotificationsScreen -> openEnableNotificationsScreen(
                navController,
                effect
            )

            is EndGameEffect.ShowAd -> activity?.let { showAd(it, viewModel, effect.rewardedAd) }
            is EndGameEffect.RequestReview -> activity?.let {
                launchReviewFlow(it, effect.reviewManager, effect.reviewInfo)
            }

            is EndGameEffect.SendTotalCoinsScore -> activity?.let {
                coroutineScope.launch {
                    submitTotalCoinsScore(it, effect.googlePlayGames, effect.totalCoins)
                }
            }

            is EndGameEffect.Share -> activity?.let { share(it, effect.text) }
            is EndGameEffect.ShareDailyChallenge -> activity?.let {
                share(it, effect.text)
            }
        }
    }
}

private fun share(activity: Activity, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    activity.startActivity(Intent.createChooser(intent, null))
}

private fun openGameScreen(
    navController: NavController,
    board: BoardModel
) {
    navController.navigateToGameFromEndGame(board.id)
}

private fun openEnableNotificationsScreen(
    navController: NavController,
    effect: EndGameEffect.OpenEnableNotificationsScreen
) {
    navController.navigateToEnableNotifications(
        effect.destination.route,
        effect.boardModel?.id.orEmpty()
    )
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

private suspend fun submitTotalCoinsScore(
    activity: Activity,
    googlePlayGames: GooglePlayGames,
    totalCoins: Long
) {
    googlePlayGames.submitTotalCoins(activity, totalCoins)
}

private fun launchReviewFlow(
    activity: Activity,
    reviewManager: ReviewManager,
    reviewInfo: ReviewInfo
) {
    reviewManager.launchReviewFlow(activity, reviewInfo)
}

@Composable
private fun HandleState(
    viewModel: EndGameViewModel,
    state: EndGameState
) {
    val callbacks = EndGameCallbacks(
        onNextClick = { viewModel.sendIntent(EndGameIntent.OnNextClick(state.board)) },
        onMenuClick = { viewModel.sendIntent(EndGameIntent.OnMenuClick) },
        onUnlockNewCardClick = { viewModel.sendIntent(EndGameIntent.OnUnlockNewCardClick) },
        onWatchAdClick = { viewModel.sendIntent(EndGameIntent.OnWatchAdClick) },
        onShareClick = { viewModel.sendIntent(EndGameIntent.OnShareClick) },
        onDailyChallengeStarsAnimationFinished = { viewModel.sendIntent(EndGameIntent.OnDailyChallengeStarsAnimationFinished) },
        onDailyChallengeShareClick = { viewModel.sendIntent(EndGameIntent.OnDailyChallengeShareClick) },
        onLevelComplete = { viewModel.sendIntent(EndGameIntent.OnLevelComplete) },
        onRewardCoinsReady = { viewModel.sendIntent(EndGameIntent.OnRewardCoinsReady) }
    )
    EndGameScreen(state, callbacks)
}

@Composable
private fun EndGameScreen(
    state: EndGameState,
    callbacks: EndGameCallbacks = EndGameCallbacks()
) {
    if (state.isDailyChallenge) {
        DailyChallengeEndGameContent(state, callbacks)
    } else {
        CasualEndGameContent(state, callbacks)
    }
}

@Preview(showBackground = true)
@Composable
private fun EndGameScreenPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                board = BoardModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.Next,
                    EndGameMenuModel.Menu
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EndGameScreenWithAdPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                board = BoardModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.WatchAd,
                    EndGameMenuModel.UnlockNewCard,
                    EndGameMenuModel.Next,
                    EndGameMenuModel.Menu
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EndGameScreenDailyChallengeThreeStarsPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 0,
                    starCount = 3,
                    timeMillis = 83456L,
                    epochDay = 42L
                ),
                isDailyChallenge = true,
                rewardedCoins = 1234,
                currentCoins = 5678
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EndGameScreenDailyChallengeTwoStarsPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 3,
                    starCount = 2,
                    timeMillis = 152789L,
                    epochDay = 15L
                ),
                isDailyChallenge = true,
                rewardedCoins = 1234,
                currentCoins = 5678
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EndGameScreenDailyChallengeOneStarPreview() {
    AppTheme {
        EndGameScreen(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 5,
                    starCount = 1,
                    timeMillis = 245123L,
                    epochDay = 7L
                ),
                isDailyChallenge = true,
                rewardedCoins = 1234,
                currentCoins = 5678
            )
        )
    }
}
