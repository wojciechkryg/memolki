package com.wojdor.memolki.ui.feature.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.menu.component.MenuContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.gameservices.GameServices
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

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
    val gameServices = koinInject<GameServices>()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            MenuEffect.OpenChooseBoardScreen -> navController.navigateToChooseBoard()
            MenuEffect.OpenCollectionScreen -> navController.navigateToCollection()
            MenuEffect.OpenLeaderboardScreen -> openLeaderboardScreen(viewModel, gameServices)
            is MenuEffect.SendTotalCoinsScore -> viewModel.viewModelScope.launch {
                gameServices.submitTotalCoins(effect.totalCoins)
            }

            is MenuEffect.SendTotalCardPairsMatchedScore -> viewModel.viewModelScope.launch {
                gameServices.submitTotalCardPairsMatched(effect.totalCardPairsMatched)
            }

            MenuEffect.OpenSettingsScreen -> navController.navigateToSettings()
            MenuEffect.OpenMoreAppsScreen -> navController.navigateToMoreApps()
            MenuEffect.OpenShopScreen -> navController.navigateToShop()
        }
    }
}

private fun openLeaderboardScreen(
    viewModel: MenuViewModel,
    gameServices: GameServices
) {
    viewModel.viewModelScope.launch {
        if (!gameServices.isAuthenticated()) {
            gameServices.signIn()
        }
        if (gameServices.isAuthenticated()) {
            gameServices.openLeaderboard()
        }
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

@Preview
@Composable
private fun MenuScreenPreview() {
    AppTheme {
        PreviewBackground {
            MenuScreen(
                state = MenuState(
                    menu = listOf(
                        MenuModel.Play,
                        MenuModel.Collection,
                        MenuModel.Leaderboard,
                        MenuModel.Settings
                    ),
                    currentApp = AppModel.FruitHalf,
                    otherAppModel = AppModel.VegetableHalf
                ),
                callbacks = MenuCallbacks()
            )
        }
    }
}

@Composable
@Preview
private fun MenuScreenWithoutMoreAppsPreview() {
    AppTheme {
        PreviewBackground {
            MenuScreen(
                state = MenuState(
                    menu = listOf(
                        MenuModel.Play,
                        MenuModel.Collection,
                        MenuModel.Leaderboard,
                        MenuModel.Settings
                    ),
                    currentApp = AppModel.FruitHalf
                ),
                callbacks = MenuCallbacks()
            )
        }
    }
}
