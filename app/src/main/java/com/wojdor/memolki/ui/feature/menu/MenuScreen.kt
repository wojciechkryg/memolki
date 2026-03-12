package com.wojdor.memolki.ui.feature.menu

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.app.navigateToChooseLevel
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToMoreApps
import com.wojdor.memolki.ui.app.navigateToSettings
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.menu.component.MenuContent
import com.wojdor.memolki.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
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
            MenuEffect.OpenChooseLevelScreen -> navController.navigateToChooseLevel()
            MenuEffect.OpenCollectionScreen -> navController.navigateToCollection()
            is MenuEffect.OpenLeaderboardScreen -> activity?.let {
                openLeaderboardScreen(
                    it,
                    viewModel,
                    effect.googlePlayGames
                )
            }

            MenuEffect.OpenSettingsScreen -> navController.navigateToSettings()
            MenuEffect.OpenMoreAppsScreen -> navController.navigateToMoreApps()
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


@Composable
private fun HandleState(
    viewModel: MenuViewModel,
    state: MenuState
) {
    val callbacks = MenuCallbacks(
        onNewGameClick = { viewModel.sendIntent(MenuIntent.OnNewGameClick) },
        onCollectionClick = { viewModel.sendIntent(MenuIntent.OnCollectionClick) },
        onLeaderboardClick = { viewModel.sendIntent(MenuIntent.OnLeaderboardClick) },
        onSettingsClick = { viewModel.sendIntent(MenuIntent.OnSettingsClick) },
        onMoreAppsClick = { viewModel.sendIntent(MenuIntent.OnMoreAppsClick) },
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
                    MenuModel.NewGame,
                    MenuModel.Collection,
                    MenuModel.Settings
                ),
                AppModel.VegetableHalf
            ),
            callbacks = MenuCallbacks()
        )
    }
}
