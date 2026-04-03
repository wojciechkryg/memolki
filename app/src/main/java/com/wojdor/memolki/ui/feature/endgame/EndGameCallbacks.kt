package com.wojdor.memolki.ui.feature.endgame

data class EndGameCallbacks(
    val onPlayAgainClick: () -> Unit = {},
    val onMenuClick: () -> Unit = {},
    val onUnlockNewCardClick: () -> Unit = {},
    val onWatchAdClick: () -> Unit = {},
    val onShareClick: () -> Unit = {},
    val onFreeCoinsClick: () -> Unit = {},
    val onDailyChallengeStarsAnimationFinished: () -> Unit = {},
    val onDailyChallengeShareClick: () -> Unit = {},
    val onLevelCompleteSoundReady: () -> Unit = {},
    val onRewardCoinsSoundReady: () -> Unit = {}
)
