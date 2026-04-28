package com.wojdor.memolki

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.wojdor.memolki.ui.app.AppNavigation
import com.wojdor.memolki.ui.app.AppViewModel
import com.wojdor.memolki.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { MainHost() }

@Composable
private fun MainHost() {
    val viewModel = koinViewModel<AppViewModel>()
    AppTheme {
        AppNavigation(
            hasPlayedTodayDailyChallenge = { viewModel.hasPlayedTodayDailyChallenge() }
        )
    }
}
