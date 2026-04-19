package com.wojdor.memolki.ui.feature.menu

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.app.navigateToChooseBoard
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToMoreApps
import com.wojdor.memolki.ui.app.navigateToSettings
import com.wojdor.memolki.ui.app.navigateToShop
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.menu.component.MenuContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.playgames.GooglePlayGames
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    LifecycleResumeEffect(Unit) {
        viewModel.sendIntent(MenuIntent.OnScreenResume)
        onPauseOrDispose {}
    }
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: MenuViewModel,
    navController: NavController
) {
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            MenuEffect.OpenChooseBoardScreen -> navController.navigateToChooseBoard()
            MenuEffect.OpenCollectionScreen -> navController.navigateToCollection()
            is MenuEffect.OpenLeaderboardScreen -> activity?.let {
                openLeaderboardScreen(
                    it,
                    viewModel,
                    effect.googlePlayGames
                )
            }

            is MenuEffect.SendTotalCoinsScore -> activity?.let {
                sendTotalCoinsScore(it, viewModel, effect.googlePlayGames, effect.totalCoins)
            }

            is MenuEffect.SendTotalCardPairsMatchedScore -> activity?.let {
                sendTotalCardPairsMatchedScore(
                    it,
                    viewModel,
                    effect.googlePlayGames,
                    effect.totalCardPairsMatched
                )
            }

            MenuEffect.OpenSettingsScreen -> navController.navigateToSettings()
            MenuEffect.OpenMoreAppsScreen -> navController.navigateToMoreApps()
            MenuEffect.OpenShopScreen -> navController.navigateToShop()
        }
    }
}

private fun openLeaderboardScreen(
    activity: Activity,
    viewModel: MenuViewModel,
    googlePlayGames: GooglePlayGames
) {
    viewModel.viewModelScope.launch {
        if (!googlePlayGames.isAuthenticated(activity)) {
            googlePlayGames.signIn(activity)
        }
        if (googlePlayGames.isAuthenticated(activity)) {
            val intent = googlePlayGames.getLeaderboardIntent(activity)
            activity.startActivityForResult(intent, REQUEST_CODE_LEADERBOARD)
        }
    }
}

private fun sendTotalCoinsScore(
    activity: Activity,
    viewModel: MenuViewModel,
    googlePlayGames: GooglePlayGames,
    totalCoins: Long
) {
    viewModel.viewModelScope.launch {
        googlePlayGames.submitTotalCoins(activity, totalCoins)
    }
}

private fun sendTotalCardPairsMatchedScore(
    activity: Activity,
    viewModel: MenuViewModel,
    googlePlayGames: GooglePlayGames,
    totalCardPairsMatched: Long
) {
    viewModel.viewModelScope.launch {
        googlePlayGames.submitTotalCardPairsMatched(activity, totalCardPairsMatched)
    }
}

@Composable
private fun HandleState(
    viewModel: MenuViewModel,
    state: MenuState
) {
    val callbacks = MenuCallbacks(
        onPlayClick = { viewModel.sendIntent(MenuIntent.OnPlayClick) },
        onCollectionClick = { viewModel.sendIntent(MenuIntent.OnCollectionClick) },
        onLeaderboardClick = { viewModel.sendIntent(MenuIntent.OnLeaderboardClick) },
        onSettingsClick = { viewModel.sendIntent(MenuIntent.OnSettingsClick) },
        onMoreAppsClick = { viewModel.sendIntent(MenuIntent.OnMoreAppsClick) },
        onDailyRewardClick = { viewModel.sendIntent(MenuIntent.OnDailyRewardClick) },
    )
    MenuScreen(state, callbacks)
}

@Composable
private fun MenuScreen(
    state: MenuState,
    callbacks: MenuCallbacks
) {
    MenuContent(state, callbacks)
}

private const val REQUEST_CODE_LEADERBOARD = 1

@Preview(showBackground = true)
@Composable
private fun MenuScreenPreview() {
    AppTheme {
        MenuScreen(
            state = MenuState(
                listOf(
                    MenuModel.Play,
                    MenuModel.Collection,
                    MenuModel.Leaderboard,
                    MenuModel.Settings
                ),
                AppModel.VegetableHalf
            ),
            callbacks = MenuCallbacks()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenWithoutMoreAppsPreview() {
    AppTheme {
        MenuScreen(
            state = MenuState(
                listOf(
                    MenuModel.Play,
                    MenuModel.Collection,
                    MenuModel.Leaderboard,
                    MenuModel.Settings
                )
            ),
            callbacks = MenuCallbacks()
        )
    }
}
