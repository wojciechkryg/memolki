package com.wojdor.memolki.ui.component

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.ui.feature.settings.component.ToggleSettingButton
import com.wojdor.memolki.ui.theme.AppTheme

@PreviewTest
@Preview(showBackground = true)
@Composable
fun BaseMenuItemUppercaseScreenshot() {
    AppTheme {
        BaseMenuItem(R.string.app_name)
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun BaseMenuItemLowercaseScreenshot() {
    AppTheme {
        BaseMenuItem(R.string.app_name, isUppercase = false)
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun BaseMenuItemDisabledScreenshot() {
    AppTheme {
        BaseMenuItem(R.string.app_name, isUppercase = false, isEnabled = false)
    }
}

@PreviewTest
@Preview
@Composable
fun CoinsAmountScreenshot() {
    AppTheme {
        CoinsAmount(
            modifier = Modifier.width(128.dp),
            coins = 1234L,
            animate = false
        )
    }
}

@PreviewTest
@Preview
@Composable
fun ToggleSettingButtonEnabledScreenshot() {
    AppTheme {
        ToggleSettingButton(
            setting = SettingModel.Music(isEnabled = true)
        )
    }
}

@PreviewTest
@Preview
@Composable
fun ToggleSettingButtonDisabledScreenshot() {
    AppTheme {
        ToggleSettingButton(
            setting = SettingModel.Music(isEnabled = false)
        )
    }
}
