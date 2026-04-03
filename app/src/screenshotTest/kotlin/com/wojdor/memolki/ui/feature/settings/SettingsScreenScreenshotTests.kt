package com.wojdor.memolki.ui.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.theme.AppTheme

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SettingsScreenEnabledScreenshot() {
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

@PreviewTest
@Preview(showBackground = true)
@Composable
fun SettingsScreenDisabledScreenshot() {
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
