package com.wojdor.memolki.ui.app

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

const val DAILY_CHALLENGE_BOARD_ID = "daily_challenge"

object Route {
    const val MENU = "menu"
    const val CHOOSE_BOARD = "choose_board"
    const val GAME_BASE = "game"
    const val GAME = "$GAME_BASE/{${NavArg.BOARD}}"
    const val END_GAME = "end_game"
    const val COLLECTION = "collection"
    const val SHOP = "shop"
    const val CARD_PAIR_DETAILS = "card_pair_details"
    const val SETTINGS = "settings"
    const val CHANGE_LANGUAGE = "change_language"
    const val MORE_APPS = "more_apps"
    const val DAILY_CHALLENGE_HISTORY = "daily_challenge_history"
    const val ENABLE_NOTIFICATIONS =
        "enable_notifications/{${NavArg.DESTINATION}}/{${NavArg.BOARD}}"
}

object RouteFlow {
    const val GAME = "game_flow"
    const val COLLECTION = "collection_flow"
    const val SETTINGS = "settings_flow"
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
    navigate(Route.GAME.replace("{${NavArg.BOARD}}", boardId)) {
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
    navigate(Route.GAME.replace("{${NavArg.BOARD}}", boardId)) {
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
            .replace("{${NavArg.DESTINATION}}", destination)
            .replace("{${NavArg.BOARD}}", boardId)
    )
}

fun NavOptionsBuilder.removeFromBackStack(route: String, isInclusive: Boolean = true) {
    popUpTo(route) {
        inclusive = isInclusive
    }
}
