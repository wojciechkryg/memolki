package com.wojdor.memolki.ui.app

import android.content.Intent
import androidx.compose.animation.EnterExitState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsScreen
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.changelanguage.ChangeLanguageScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardScreen
import com.wojdor.memolki.ui.feature.collection.CollectionScreen
import com.wojdor.memolki.ui.feature.dailychallengehistory.DailyChallengeHistoryScreen
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationsScreen
import com.wojdor.memolki.ui.feature.endgame.EndGameScreen
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.GameIntent
import com.wojdor.memolki.ui.feature.game.GameScreen
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.feature.menu.MenuScreen
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsScreen
import com.wojdor.memolki.ui.feature.settings.SettingsScreen
import com.wojdor.memolki.ui.feature.shop.ShopScreen
import com.wojdor.memolki.util.notification.DeepLinkBuilder

@Composable
fun AppNavigation(
    onNewIntent: Intent? = null,
    onIntentHandled: () -> Unit = {},
    hasPlayedTodayDailyChallenge: suspend () -> Boolean = { true }
) {
    val navController = rememberNavController()
    LaunchedEffect(onNewIntent) {
        onNewIntent?.let {
            navigateFromDeepLink(navController, it, hasPlayedTodayDailyChallenge)
            onIntentHandled()
        }
    }
    NavHost(navController, startDestination = Route.MENU) {
        menuScreen(navController)
        gameFlow(navController)
        collectionFlow(navController)
        settingsFlow(navController)
        moreAppsScreen()
        enableNotificationsScreen(navController)
    }
}

private fun NavGraphBuilder.menuScreen(navController: NavController) {
    composable(
        route = Route.MENU,
        enterTransition = {
            when (initialState.destination.route) {
                Route.CHOOSE_BOARD, Route.GAME, Route.END_GAME -> slideInLeft
                Route.ENABLE_NOTIFICATIONS -> slideInTop
                Route.COLLECTION -> slideInRight
                Route.SETTINGS -> slideInTop
                Route.MORE_APPS -> slideInBottom
                else -> slideInBottom
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.CHOOSE_BOARD, Route.GAME, Route.END_GAME -> slideOutLeft
                Route.ENABLE_NOTIFICATIONS -> slideOutTop
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
        startDestination = Route.CHOOSE_BOARD,
        route = RouteFlow.GAME
    ) {
        chooseBoardScreen(navController)
        gameScreen(navController)
        endGameScreen(navController)
        dailyChallengeHistoryScreen(navController)
    }
}

private fun NavGraphBuilder.chooseBoardScreen(navController: NavController) {
    composable(
        route = Route.CHOOSE_BOARD,
        enterTransition = {
            when (initialState.destination.route) {
                Route.MENU -> slideInRight
                else -> slideInLeft
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.MENU, Route.COLLECTION -> slideOutRight
                else -> slideOutLeft
            }
        },
    ) {
        ChooseBoardScreen(navController = navController)
    }
}

private fun NavGraphBuilder.gameScreen(navController: NavController) {
    composable(
        route = Route.GAME,
        arguments = listOf(navArgument(AppNavigation.BOARD_ARG) {
            type = NavType.StringType
        }),
        enterTransition = {
            when (initialState.destination.route) {
                Route.END_GAME -> slideInLeft
                Route.ENABLE_NOTIFICATIONS -> slideInTop
                else -> slideInRight
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.MENU -> slideOutRight
                else -> slideOutLeft
            }
        }
    ) { backStackEntry ->
        val boardId = backStackEntry.arguments?.getString(AppNavigation.BOARD_ARG)
        val isDailyChallenge = boardId == DAILY_CHALLENGE_BOARD_ID
        val gameViewModel = getGameViewModel(backStackEntry, navController)
        LaunchedEffect(boardId) {
            boardId?.let { board ->
                gameViewModel.sendIntent(GameIntent.OnBoardStart(board, isDailyChallenge))
            }
        }
        GameScreen(
            navController = navController,
            viewModel = gameViewModel,
            endGameViewModel = getEndGameViewModel(backStackEntry, navController)
        )
    }
}

private fun NavGraphBuilder.endGameScreen(navController: NavController) {
    composable(
        route = Route.END_GAME,
        enterTransition = {
            when (initialState.destination.route) {
                Route.SHOP -> slideInBottom
                else -> slideInRight
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.ENABLE_NOTIFICATIONS -> slideOutTop
                Route.SHOP -> slideOutBottom
                else -> slideOutRight
            }
        }
    ) { backStackEntry ->
        EndGameScreen(
            navController = navController,
            viewModel = getEndGameViewModel(backStackEntry, navController),
            gameViewModel = getGameViewModel(backStackEntry, navController),
            isEnterAnimationFinished = transition.currentState == EnterExitState.Visible
        )
    }
}

private fun NavGraphBuilder.dailyChallengeHistoryScreen(navController: NavController) {
    composable(
        route = Route.DAILY_CHALLENGE_HISTORY,
        enterTransition = { slideInRight },
        exitTransition = { slideOutRight }
    ) {
        DailyChallengeHistoryScreen(navController = navController)
    }
}

private fun NavGraphBuilder.enableNotificationsScreen(navController: NavController) {
    composable(
        route = Route.ENABLE_NOTIFICATIONS,
        arguments = listOf(
            navArgument(AppNavigation.DESTINATION_ARG) { type = NavType.StringType },
            navArgument(AppNavigation.BOARD_ARG) { type = NavType.StringType }
        ),
        enterTransition = { slideInBottom },
        exitTransition = { slideOutBottom }
    ) {
        EnableNotificationsScreen(navController = navController)
    }
}

private fun NavGraphBuilder.collectionFlow(navController: NavController) {
    navigation(
        startDestination = Route.COLLECTION,
        route = RouteFlow.COLLECTION
    ) {
        collectionScreen(navController)
        shopScreen(navController)
        cardPairDetailsScreen(navController)
    }
}

private fun NavGraphBuilder.collectionScreen(navController: NavController) {
    composable(
        route = Route.COLLECTION,
        enterTransition = {
            when (initialState.destination.route) {
                Route.SHOP -> slideInBottom
                Route.ENABLE_NOTIFICATIONS -> slideInTop
                Route.CARD_PAIR_DETAILS -> slideInRight
                else -> slideInLeft
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Route.SHOP -> slideOutBottom
                Route.ENABLE_NOTIFICATIONS -> slideOutTop
                Route.CARD_PAIR_DETAILS -> slideOutRight
                else -> slideOutLeft
            }
        }
    ) { backStackEntry ->
        CollectionScreen(
            cardPairDetailsViewModel = getCardPairDetailsViewModel(backStackEntry, navController),
            navController = navController
        )
    }
}

private fun NavGraphBuilder.shopScreen(navController: NavController) {
    composable(
        route = Route.SHOP,
        enterTransition = { slideInTop },
        exitTransition = { slideOutTop }
    ) {
        ShopScreen(navController = navController)
    }
}

private fun NavGraphBuilder.cardPairDetailsScreen(navController: NavController) {
    composable(
        route = Route.CARD_PAIR_DETAILS,
        enterTransition = { slideInLeft },
        exitTransition = { slideOutLeft }
    ) { backStackEntry ->
        CardPairDetailsScreen(
            viewModel = getCardPairDetailsViewModel(backStackEntry, navController)
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

fun NavController.navigateToChooseBoard() {
    navigate(Route.CHOOSE_BOARD)
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

fun NavController.navigateToGame(boardId: String) {
    navigate(Route.GAME.replace("{${AppNavigation.BOARD_ARG}}", boardId)) {
        removeFromBackStack(Route.CHOOSE_BOARD)
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

fun NavController.navigateToGameFromEndGame(boardId: String) {
    navigate(Route.GAME.replace("{${AppNavigation.BOARD_ARG}}", boardId)) {
        removeFromBackStack(Route.END_GAME)
    }
}

fun NavController.navigateToDailyChallenge() {
    navigateToGame(DAILY_CHALLENGE_BOARD_ID)
}

fun NavController.navigateToDailyChallengeHistory() {
    navigate(Route.DAILY_CHALLENGE_HISTORY)
}

fun NavController.navigateToShop() {
    navigate(Route.SHOP) {
        removeFromBackStack(Route.SHOP)
    }
}

fun NavController.navigateToCardPairDetailsScreen() {
    navigate(Route.CARD_PAIR_DETAILS)
}

fun NavController.navigateToEnableNotifications(destination: String, boardId: String = "") {
    navigate(
        Route.ENABLE_NOTIFICATIONS
            .replace("{${AppNavigation.DESTINATION_ARG}}", destination)
            .replace("{${AppNavigation.BOARD_ARG}}", boardId)
    )
}

private fun NavController.navigateToGameFromDeepLink(board: String) {
    navigate(Route.GAME.replace("{${AppNavigation.BOARD_ARG}}", board)) {
        removeFromBackStack(Route.MENU, isInclusive = false)
    }
}

private suspend fun navigateFromDeepLink(
    navController: NavController,
    intent: Intent,
    hasPlayedTodayDailyChallenge: suspend () -> Boolean
) {
    val screen = intent.data?.host ?: return
    val pathSegments = intent.data?.pathSegments.orEmpty()
    when (screen) {
        DeepLinkBuilder.SCREEN_SHOP -> navController.navigateToShop()
        DeepLinkBuilder.SCREEN_COLLECTION -> navController.navigateToCollection()
        DeepLinkBuilder.SCREEN_MORE_APPS -> navController.navigateToMoreApps()
        DeepLinkBuilder.SCREEN_GAME -> {
            val board = pathSegments.firstOrNull().orEmpty()
            navController.navigateToGameFromDeepLink(board)
        }

        DeepLinkBuilder.SCREEN_DAILY_CHALLENGE -> {
            if (!hasPlayedTodayDailyChallenge()) {
                navController.navigateToGameFromDeepLink(DAILY_CHALLENGE_BOARD_ID)
            }
        }
    }
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
    return koinViewModel(viewModelStoreOwner = flowBackStackEntry)
}

@Composable
private fun getEndGameViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): EndGameViewModel {
    val flowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.GAME)
    }
    return koinViewModel(viewModelStoreOwner = flowBackStackEntry)
}

@Composable
private fun getCardPairDetailsViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): CardPairDetailsViewModel {
    val flowBackStackEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(RouteFlow.COLLECTION)
    }
    return koinViewModel(viewModelStoreOwner = flowBackStackEntry)
}

private const val DAILY_CHALLENGE_BOARD_ID = "daily_challenge"

internal object Route {
    const val MENU = "menu"
    const val CHOOSE_BOARD = "choose_board"
    const val GAME_BASE = "game"
    const val GAME = "$GAME_BASE/{${AppNavigation.BOARD_ARG}}"
    const val END_GAME = "end_game"
    const val COLLECTION = "collection"
    const val SHOP = "shop"
    const val CARD_PAIR_DETAILS = "card_pair_details"
    const val SETTINGS = "settings"
    const val CHANGE_LANGUAGE = "change_language"
    const val MORE_APPS = "more_apps"
    const val DAILY_CHALLENGE_HISTORY = "daily_challenge_history"
    const val ENABLE_NOTIFICATIONS =
        "enable_notifications/{${AppNavigation.DESTINATION_ARG}}/{${AppNavigation.BOARD_ARG}}"
}

private object RouteFlow {
    const val GAME = "game_flow"
    const val COLLECTION = "collection_flow"
    const val SETTINGS = "settings_flow"
}

object AppNavigation {
    const val DESTINATION_ARG = "destination"
    const val BOARD_ARG = "board"
}
