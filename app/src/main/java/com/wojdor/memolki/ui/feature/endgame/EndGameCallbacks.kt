package com.wojdor.memolki.ui.feature.endgame

data class EndGameCallbacks(
    val onPlayAgainClick: () -> Unit = {},
    val onMenuClick: () -> Unit = {},
    val onUnlockNewCardClick: () -> Unit = {},
    val onWatchAdClick: () -> Unit = {}
)
