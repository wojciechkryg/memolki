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
    }
}

fun NavController.navigateToMenu() {
    navigate(Route.MENU)
}

object Route {
    const val MENU = "menu"
}
