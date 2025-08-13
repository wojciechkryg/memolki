package com.wojdor.memolki.ui.feature.menu

data class MenuCallbacks(
    val onNewGameClick: () -> Unit = {},
    val onCollectionClick: () -> Unit = {},
    val onSettingsClick: () -> Unit = {}
)
