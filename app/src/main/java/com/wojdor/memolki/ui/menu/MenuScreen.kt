package com.wojdor.memolki.ui.menu

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.base.CollectUiEffects

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
            MenuEffect.OpenChooseLevelScreen -> TODO()
            MenuEffect.OpenOptionsScreen -> TODO()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: MenuViewModel,
    state: MenuState
) {
    MenuState(state)
}

@Composable
private fun MenuState(state: MenuState) {
    Text(text = stringResource(R.string.app_name))
}
