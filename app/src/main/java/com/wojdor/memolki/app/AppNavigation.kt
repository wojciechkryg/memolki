package com.wojdor.memolki.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wojdor.memolki.ui.menu.MenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.MENU) {
        composable(Route.MENU) { MenuScreen(navController = navController) }
        composable(Route.CHOOSE_LEVEL) { TODO() }
        composable(Route.OPTIONS) { TODO() }
    }
}

fun NavController.navigateToMenu() {
    navigate(Route.MENU)
}

object Route {
    const val MENU = "menu"
    const val CHOOSE_LEVEL = "chose_level"
    const val OPTIONS = "options"
}
