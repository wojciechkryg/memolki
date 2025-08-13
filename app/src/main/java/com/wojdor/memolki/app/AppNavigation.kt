package com.wojdor.memolki.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelScreen
import com.wojdor.memolki.ui.feature.collection.CollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuScreen
import com.wojdor.memolki.ui.feature.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.MENU) {
        composable(
            route = Route.MENU,
            exitTransition = {
                when (targetState.destination.route) {
                    Route.CHOOSE_LEVEL -> slideOutLeft
                    Route.COLLECTION -> slideOutRight
                    Route.OPTIONS -> slideOutTop
                    else -> slideOutBottom
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Route.CHOOSE_LEVEL -> slideInLeft
                    Route.COLLECTION -> slideInRight
                    Route.OPTIONS -> slideInTop
                    else -> slideInBottom
                }
            }
        ) { MenuScreen(navController = navController) }
        composable(
            route = Route.CHOOSE_LEVEL,
            enterTransition = { slideInRight },
            popExitTransition = { slideOutRight }
        ) { ChooseLevelScreen(navController = navController) }
        composable(
            route = Route.COLLECTION,
            enterTransition = { slideInLeft },
            popExitTransition = { slideOutLeft }
        ) { CollectionScreen(navController = navController) }
        composable(
            route = Route.OPTIONS,
            enterTransition = { slideInBottom },
            popExitTransition = { slideOutBottom }
        ) { SettingsScreen(navController = navController) }
    }
}

fun NavController.navigateToChooseLevel() {
    navigate(Route.CHOOSE_LEVEL)
}

fun NavController.navigateToCollection() {
    navigate(Route.COLLECTION)
}

fun NavController.navigateToOptions() {
    navigate(Route.OPTIONS)
}

private object Route {
    const val MENU = "menu"
    const val CHOOSE_LEVEL = "chose_level"
    const val COLLECTION = "collection"
    const val OPTIONS = "options"
}
