package com.wojdor.memolki.util.notification

object DeepLinkBuilder {

    const val SCHEME = "memolki"
    const val EXTRA_SCREEN = "screen"
    const val EXTRA_LEVEL = "level"
    const val SCREEN_SHOP = "shop"
    const val SCREEN_COLLECTION = "collection"
    const val SCREEN_MORE_APPS = "more_apps"
    const val SCREEN_GAME = "game"
    private const val LEVEL_AUTO = "auto"

    fun buildScreenUri(screen: String): String = "$SCHEME://$screen"

    fun buildUri(screen: String?, level: String? = null): String? = when (screen) {
        SCREEN_SHOP, SCREEN_COLLECTION, SCREEN_MORE_APPS -> buildScreenUri(screen)
        SCREEN_GAME -> "$SCHEME://$SCREEN_GAME/${level ?: LEVEL_AUTO}"
        else -> null
    }
}
