package com.wojdor.memolki.ui.feature.endgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToEnableNotifications
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.endgame.component.CasualEndGameContent
import com.wojdor.memolki.ui.feature.endgame.component.DailyChallengeEndGameContent
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.extension.TextSharer
import com.wojdor.memolki.util.gameservices.GameServices
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EndGameScreen(
    viewModel: EndGameViewModel = koinViewModel(),
    gameViewModel: GameViewModel,
    isEnterAnimationFinished: Boolean,
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(isEnterAnimationFinished) {
        if (isEnterAnimationFinished) {
            gameViewModel.sendIntent(GameIntent.OnResetState)
        }
    }
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
    val coroutineScope = rememberCoroutineScope()
    val gameServices = koinInject<GameServices>()
    val textSharer = koinInject<TextSharer>()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is EndGameEffect.OpenGameScreen -> openGameScreen(navController, effect.boardModel)
            EndGameEffect.OpenMenuScreen -> navController.navigateToMenu()
            EndGameEffect.OpenCollectionScreen -> navController.navigateToCollection()
            is EndGameEffect.OpenEnableNotificationsScreen -> openEnableNotificationsScreen(
                navController,
                effect
            )

            is EndGameEffect.ShowAd -> showAd(viewModel, effect.rewardedAd)
            is EndGameEffect.SendTotalCoinsScore -> coroutineScope.launch {
                gameServices.submitTotalCoins(effect.totalCoins)
            }

            is EndGameEffect.Share -> textSharer.share(effect.text)
            is EndGameEffect.ShareDailyChallenge -> textSharer.share(effect.text)
        }
    }
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
    viewModel: EndGameViewModel,
    rewardedAd: RewardedAd
) {
    rewardedAd.show(
        onGrantReward = { viewModel.sendIntent(EndGameIntent.OnAdReward) },
        onAdDismiss = { viewModel.sendIntent(EndGameIntent.OnAdDismiss(it)) }
    )
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

@Preview
@Composable
private fun EndGameScreenPreview() {
    AppTheme {
        PreviewBackground {
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
}

@Preview
@Composable
private fun EndGameScreenWithAdPreview() {
    AppTheme {
        PreviewBackground {
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
}

@Preview
@Composable
private fun EndGameScreenDailyChallengeThreeStarsPreview() {
    AppTheme {
        PreviewBackground {
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
}

@Preview
@Composable
private fun EndGameScreenDailyChallengeTwoStarsPreview() {
    AppTheme {
        PreviewBackground {
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
}

@Preview
@Composable
private fun EndGameScreenDailyChallengeOneStarPreview() {
    AppTheme {
        PreviewBackground {
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
}
