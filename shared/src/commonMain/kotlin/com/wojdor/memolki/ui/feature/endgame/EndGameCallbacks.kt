package com.wojdor.memolki.ui.feature.endgame

data class EndGameCallbacks(
    val onNextClick: () -> Unit = {},
    val onMenuClick: () -> Unit = {},
    val onUnlockNewCardClick: () -> Unit = {},
    val onWatchAdClick: () -> Unit = {},
    val onShareClick: () -> Unit = {},
    val onDailyChallengeStarsAnimationFinished: () -> Unit = {},
    val onDailyChallengeShareClick: () -> Unit = {},
    val onLevelComplete: () -> Unit = {},
    val onRewardCoinsReady: () -> Unit = {}
)
