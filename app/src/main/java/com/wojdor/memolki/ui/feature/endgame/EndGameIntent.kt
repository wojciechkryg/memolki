package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class EndGameIntent : UiIntent {
    data class OnCasualEndGameShow(val boardModel: BoardModel, val level: Long) : EndGameIntent()
    data class OnDailyChallengeEndGameShow(
        val boardModel: BoardModel,
        val dailyChallengeModel: DailyChallengeModel
    ) : EndGameIntent()

    data class OnNextClick(val boardModel: BoardModel) : EndGameIntent()
    object OnMenuClick : EndGameIntent()
    object OnUnlockNewCardClick : EndGameIntent()
    object OnWatchAdClick : EndGameIntent()
    object OnAdReward : EndGameIntent()
    data class OnAdDismiss(val wasRewardGranted: Boolean) : EndGameIntent()
    object OnShareClick : EndGameIntent()
    object OnScreenResume : EndGameIntent()
    object OnDailyChallengeStarsAnimationFinished : EndGameIntent()
    object OnDailyChallengeShareClick : EndGameIntent()
    object OnLevelComplete : EndGameIntent()
    object OnRewardCoinsReady : EndGameIntent()
}
