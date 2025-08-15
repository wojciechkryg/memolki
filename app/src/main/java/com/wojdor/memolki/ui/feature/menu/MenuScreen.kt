package com.wojdor.memolki.ui.feature.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.app.navigateToChooseLevel
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToOptions
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.menu.component.MenuItem
import com.wojdor.memolki.ui.theme.AppTheme

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
    CollectUiEffects(viewModel) {
        when (it) {
            MenuEffect.OpenChooseLevelScreen -> navController.navigateToChooseLevel()
            MenuEffect.OpenCollectionScreen -> navController.navigateToCollection()
            MenuEffect.OpenSettingsScreen -> navController.navigateToOptions()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: MenuViewModel,
    state: MenuState
) {
    val callbacks = MenuCallbacks(
        onNewGameClick = { viewModel.sendIntent(MenuIntent.OnNewGameClicked) },
        onCollectionClick = { viewModel.sendIntent(MenuIntent.OnCollectionClicked) },
        onSettingsClick = { viewModel.sendIntent(MenuIntent.OnSettingsClicked) },
    )
    MenuScreen(state, callbacks)
}

@Composable
private fun MenuScreen(
    state: MenuState,
    callbacks: MenuCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(320.dp),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null
        )
        state.menu.forEach { menuItem ->
            Spacer(modifier = Modifier.height(16.dp))
            when (menuItem) {
                is MenuModel.NewGame -> MenuItem(
                    textId = menuItem.textId,
                    onClick = callbacks.onNewGameClick
                )

                is MenuModel.Collection -> MenuItem(
                    textId = menuItem.textId,
                    onClick = callbacks.onCollectionClick
                )

                is MenuModel.Settings -> MenuItem(
                    textId = R.string.settings,
                    onClick = callbacks.onSettingsClick
                )
            }
        }
    }
}

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
                )
            ),
            callbacks = MenuCallbacks()
        )
    }
}
