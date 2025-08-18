package com.wojdor.memolki.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelScreen
import com.wojdor.memolki.ui.feature.collection.CollectionScreen
import com.wojdor.memolki.ui.feature.game.GameScreen
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.feature.menu.MenuScreen
import com.wojdor.memolki.ui.feature.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.MENU) {
        composable(
            route = Route.MENU,
            enterTransition = {
                when (initialState.destination.route) {
                    Route.CHOOSE_LEVEL, Route.GAME -> slideInLeft
                    Route.COLLECTION -> slideInRight
                    Route.OPTIONS -> slideInTop
                    else -> slideInBottom
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Route.CHOOSE_LEVEL, Route.GAME -> slideOutLeft
                    Route.COLLECTION -> slideOutRight
                    Route.OPTIONS -> slideOutTop
                    else -> slideOutBottom
                }
            }
        ) { MenuScreen(navController = navController) }
        navigation(
            startDestination = Route.CHOOSE_LEVEL,
            route = RouteFlow.GAME
        ) {
            composable(
                route = Route.CHOOSE_LEVEL,
                enterTransition = {
                    when (initialState.destination.route) {
                        Route.MENU -> slideInRight
                        else -> slideInLeft
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Route.MENU -> slideOutRight
                        else -> slideOutLeft
                    }
                },
            ) {
                ChooseLevelScreen(
                    navController = navController,
                    gameViewModel = getGameViewModel(it, navController)
                )
            }
            composable(
                route = Route.GAME,
                enterTransition = { slideInRight },
                exitTransition = { slideOutRight }
            ) {
                GameScreen(
                    navController = navController,
                    viewModel = getGameViewModel(it, navController)
                )
            }
        }
        composable(
            route = Route.COLLECTION,
            enterTransition = { slideInLeft },
            exitTransition = { slideOutLeft }
        ) { CollectionScreen(navController = navController) }
        composable(
            route = Route.OPTIONS,
            enterTransition = { slideInBottom },
            exitTransition = { slideOutBottom }
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

fun NavController.navigateToGame() {
    navigate(Route.GAME) {
        removeFromBackStack(Route.CHOOSE_LEVEL)
    }
}

private fun NavOptionsBuilder.removeFromBackStack(route: String) {
    popUpTo(route) {
        inclusive = true
    }
}

@Composable
private fun getGameViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): GameViewModel {
    val gameFlowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.GAME)
    }
    return hiltViewModel(gameFlowBackStackEntry)
}


private object Route {
    const val MENU = "menu"
    const val CHOOSE_LEVEL = "chose_level"
    const val GAME = "game"
    const val COLLECTION = "collection"
    const val OPTIONS = "options"
}

private object RouteFlow {
    const val GAME = "game_flow"
}
