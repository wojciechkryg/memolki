package com.wojdor.memolki.ui.feature.settings

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
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.app.navigateToChangeLanguage
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.feature.settings.component.ToggleSettingButton
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingXL

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is SettingsEffect.OpenChangeLanguageScreen -> navController.navigateToChangeLanguage()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: SettingsViewModel,
    state: SettingsState
) {
    val callbacks = SettingsCallbacks(
        onSettingToggle = { viewModel.sendIntent(SettingsIntent.OnSettingClick(it)) },
        onLanguageClick = { viewModel.sendIntent(SettingsIntent.OnLanguageClick) }
    )
    SettingsScreen(state, callbacks)
}

@Composable
private fun SettingsScreen(
    state: SettingsState,
    callbacks: SettingsCallbacks = SettingsCallbacks()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.settings.forEach { setting ->
            ToggleSettingButton(setting = setting) {
                callbacks.onSettingToggle(setting)
            }
            Spacer(modifier = Modifier.height(spacingXL))
        }
        BaseMenuItem(textId = R.string.setting_language) {
            callbacks.onLanguageClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            state = SettingsState(
                listOf(
                    SettingModel.Music(true),
                    SettingModel.Sound(true),
                    SettingModel.Vibration(true)
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenDisabledPreview() {
    AppTheme {
        SettingsScreen(
            state = SettingsState(
                listOf(
                    SettingModel.Music(false),
                    SettingModel.Sound(false),
                    SettingModel.Vibration(false)
                )
            )
        )
    }
}
