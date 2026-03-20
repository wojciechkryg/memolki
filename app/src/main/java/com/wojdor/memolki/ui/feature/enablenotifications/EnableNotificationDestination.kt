package com.wojdor.memolki.ui.feature.enablenotifications

import com.wojdor.memolki.ui.app.Route

enum class EnableNotificationDestination(val route: String) {
    GAME(Route.GAME),
    MENU(Route.MENU),
    COLLECTION(Route.COLLECTION),
    SHOP(Route.SHOP);

    companion object {
        fun fromRoute(route: String): EnableNotificationDestination {
            return entries.find { it.route == route } ?: MENU
        }
    }
}
