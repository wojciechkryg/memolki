package com.wojdor.memolki

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.wojdor.memolki.ui.app.AppNavigation
import com.wojdor.memolki.ui.app.AppViewModel
import com.wojdor.memolki.ui.app.iosEdgeSwipeBack
import com.wojdor.memolki.ui.theme.AppColors
import com.wojdor.memolki.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { MainHost() }

@Composable
private fun MainHost() {
    val viewModel = koinViewModel<AppViewModel>()
    val navController = rememberNavController()
    val layoutDirection = LocalLayoutDirection.current
    AppTheme {
        Scaffold(
            containerColor = AppColors.Primary,
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .iosEdgeSwipeBack(layoutDirection) { navController.popBackStack() }
                ) {
                    AppNavigation(
                        navController = navController,
                        hasPlayedTodayDailyChallenge = { viewModel.hasPlayedTodayDailyChallenge() }
                    )
                }
            }
        )
    }
}
