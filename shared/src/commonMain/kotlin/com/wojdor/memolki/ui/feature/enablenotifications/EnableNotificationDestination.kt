package com.wojdor.memolki.ui.feature.enablenotifications

enum class EnableNotificationDestination(val route: String) {
    GAME("game"),
    MENU("menu"),
    COLLECTION("collection"),
    SHOP("shop");

    companion object {
        fun fromRoute(route: String): EnableNotificationDestination {
            return entries.find { it.route == route } ?: MENU
        }
    }
}
