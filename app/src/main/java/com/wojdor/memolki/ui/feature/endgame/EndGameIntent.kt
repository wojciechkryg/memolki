package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class EndGameIntent : UiIntent {
    data class OnEndGameShow(val levelModel: LevelModel) : EndGameIntent()
    data class OnPlayAgainClick(val levelModel: LevelModel) : EndGameIntent()
    object OnMenuClick : EndGameIntent()
    object OnUnlockNewCardClick : EndGameIntent()
    object OnWatchAdClick : EndGameIntent()
    object OnAdReward : EndGameIntent()
    data class OnAdDismiss(val wasRewardGranted: Boolean) : EndGameIntent()
}
