package com.wojdor.memolki.ui.feature.menu

data class MenuCallbacks(
    val onStartGameClick: () -> Unit = {},
    val onCollectionClick: () -> Unit = {},
    val onSettingsClick: () -> Unit = {}
)
