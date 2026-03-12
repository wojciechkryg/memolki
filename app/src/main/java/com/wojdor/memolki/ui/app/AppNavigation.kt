package com.wojdor.memolki.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsScreen
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.changelanguage.ChangeLanguageScreen
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelScreen
import com.wojdor.memolki.ui.feature.collection.CollectionScreen
import com.wojdor.memolki.ui.feature.endgame.EndGameScreen
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.GameScreen
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.feature.menu.MenuScreen
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsScreen
import com.wojdor.memolki.ui.feature.settings.SettingsScreen
import com.wojdor.memolki.ui.feature.shop.ShopScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.MENU) {
        menuScreen(navController)
        gameFlow(navController)
        collectionFlow(navController)
        settingsFlow(navController)
        moreAppsScreen()
    }
}

private fun NavGraphBuilder.menuScreen(navController: NavController) {
    composable(
        route = Route.MENU,
        enterTransition = {
            when (initialState.destination.route) {
                Route.CHOOSE_LEVEL, Route.GAME, Route.END_GAME -> slideInLeft
                Route.COLLECTION -> slideInRight
                Route.SETTINGS -> slideInTop
                Route.MORE_APPS -> slideInBottom
                else -> slideInBottom
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.CHOOSE_LEVEL, Route.GAME, Route.END_GAME -> slideOutLeft
                Route.COLLECTION -> slideOutRight
                Route.SETTINGS -> slideOutTop
                Route.MORE_APPS -> slideOutBottom
                else -> slideOutBottom
            }
        }
    ) {
        MenuScreen(navController = navController)
    }
}

private fun NavGraphBuilder.gameFlow(navController: NavController) {
    navigation(
        startDestination = Route.CHOOSE_LEVEL,
        route = RouteFlow.GAME
    ) {
        chooseLevelScreen(navController)
        gameScreen(navController)
        endGameScreen(navController)
    }
}

private fun NavGraphBuilder.chooseLevelScreen(navController: NavController) {
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
}

private fun NavGraphBuilder.gameScreen(navController: NavController) {
    composable(
        route = Route.GAME,
        enterTransition = {
            when (initialState.destination.route) {
                Route.END_GAME -> slideInLeft
                else -> slideInRight
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.MENU -> slideOutRight
                else -> slideOutLeft
            }
        }
    ) {
        GameScreen(
            navController = navController,
            viewModel = getGameViewModel(it, navController),
            endGameViewModel = getEndGameViewModel(it, navController)
        )
    }
}

private fun NavGraphBuilder.endGameScreen(navController: NavController) {
    composable(
        route = Route.END_GAME,
        enterTransition = { slideInRight },
        exitTransition = { slideOutRight }
    ) {
        EndGameScreen(
            navController = navController,
            viewModel = getEndGameViewModel(it, navController),
            gameViewModel = getGameViewModel(it, navController)
        )
    }
}

private fun NavGraphBuilder.collectionFlow(navController: NavController) {
    navigation(
        startDestination = Route.COLLECTION,
        route = RouteFlow.COLLECTION
    ) {
        collectionScreen(navController)
        shopScreen()
        cardPairDetailsScreen(navController)
    }
}

private fun NavGraphBuilder.collectionScreen(navController: NavController) {
    composable(
        route = Route.COLLECTION,
        enterTransition = {
            when (initialState.destination.route) {
                Route.SHOP -> slideInBottom
                Route.CARD_PAIR_DETAILS -> slideInRight
                else -> slideInLeft
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.SHOP -> slideOutBottom
                Route.CARD_PAIR_DETAILS -> slideOutRight
                else -> slideOutLeft
            }
        }
    ) {
        CollectionScreen(
            cardPairDetailsViewModel = getCardPairDetailsViewModel(it, navController),
            navController = navController
        )
    }
}

private fun NavGraphBuilder.shopScreen() {
    composable(
        route = Route.SHOP,
        enterTransition = { slideInTop },
        exitTransition = { slideOutTop }
    ) {
        ShopScreen()
    }
}

private fun NavGraphBuilder.cardPairDetailsScreen(navController: NavController) {
    composable(
        route = Route.CARD_PAIR_DETAILS,
        enterTransition = { slideInLeft },
        exitTransition = { slideOutLeft }
    ) {
        CardPairDetailsScreen(
            viewModel = getCardPairDetailsViewModel(it, navController),
        )
    }
}

private fun NavGraphBuilder.settingsFlow(navController: NavController) {
    navigation(
        startDestination = Route.SETTINGS,
        route = RouteFlow.SETTINGS
    ) {
        settingsScreen(navController)
        changeLanguageScreen(navController)
    }
}

private fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(
        route = Route.SETTINGS,
        enterTransition = {
            when (initialState.destination.route) {
                Route.CHANGE_LANGUAGE -> slideInTop
                else -> slideInBottom
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.CHANGE_LANGUAGE -> slideOutTop
                else -> slideOutBottom
            }
        }
    ) {
        SettingsScreen(navController = navController)
    }
}

private fun NavGraphBuilder.changeLanguageScreen(navController: NavController) {
    composable(
        route = Route.CHANGE_LANGUAGE,
        enterTransition = { slideInBottom },
        exitTransition = { slideOutBottom }
    ) {
        ChangeLanguageScreen(navController = navController)
    }
}

private fun NavGraphBuilder.moreAppsScreen() {
    composable(
        route = Route.MORE_APPS,
        enterTransition = { slideInTop },
        exitTransition = { slideOutTop }
    ) {
        MoreAppsScreen()
    }
}

fun NavController.navigateToChooseLevel() {
    navigate(Route.CHOOSE_LEVEL)
}

fun NavController.navigateToCollection() {
    navigate(Route.COLLECTION) {
        removeFromBackStack(Route.MENU, isInclusive = false)
    }
}

fun NavController.navigateToSettings() {
    navigate(Route.SETTINGS)
}

fun NavController.navigateToChangeLanguage() {
    navigate(Route.CHANGE_LANGUAGE)
}

fun NavController.navigateToMoreApps() {
    navigate(Route.MORE_APPS)
}

fun NavController.navigateToGame() {
    navigate(Route.GAME) {
        removeFromBackStack(Route.CHOOSE_LEVEL)
    }
}

fun NavController.navigateToEndGame() {
    navigate(Route.END_GAME) {
        removeFromBackStack(Route.GAME)
    }
}

fun NavController.navigateToMenu() {
    navigate(Route.MENU) {
        removeFromBackStack(Route.MENU)
    }
}

fun NavController.navigateToGameFromEndGame() {
    navigate(Route.GAME) {
        removeFromBackStack(Route.END_GAME)
    }
}

fun NavController.navigateToShop() {
    navigate(Route.SHOP)
}

fun NavController.navigateToCardPairDetailsScreen() {
    navigate(Route.CARD_PAIR_DETAILS)
}

private fun NavOptionsBuilder.removeFromBackStack(route: String, isInclusive: Boolean = true) {
    popUpTo(route) {
        inclusive = isInclusive
    }
}

@Composable
private fun getGameViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): GameViewModel {
    val flowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.GAME)
    }
    return hiltViewModel(flowBackStackEntry)
}

@Composable
private fun getEndGameViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): EndGameViewModel {
    val flowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.GAME)
    }
    return hiltViewModel(flowBackStackEntry)
}

@Composable
private fun getCardPairDetailsViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): CardPairDetailsViewModel {
    val flowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.COLLECTION)
    }
    return hiltViewModel(flowBackStackEntry)
}

private object Route {
    const val MENU = "menu"
    const val CHOOSE_LEVEL = "chose_level"
    const val GAME = "game"
    const val END_GAME = "end_game"
    const val COLLECTION = "collection"
    const val SHOP = "shop"
    const val CARD_PAIR_DETAILS = "card_pair_details"
    const val SETTINGS = "settings"
    const val CHANGE_LANGUAGE = "change_language"
    const val MORE_APPS = "more_apps"
}

private object RouteFlow {
    const val GAME = "game_flow"
    const val COLLECTION = "collection_flow"
    const val SETTINGS = "settings_flow"
}
