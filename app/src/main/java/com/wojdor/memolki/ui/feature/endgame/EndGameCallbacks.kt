package com.wojdor.memolki.ui.feature.endgame

data class EndGameCallbacks(
    val onPLayAgainClick: () -> Unit = {},
    val onMenuClick: () -> Unit = {}
)
