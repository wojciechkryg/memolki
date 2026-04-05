package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class EndGameIntent : UiIntent {
    data class OnCasualEndGameShow(val levelModel: LevelModel) : EndGameIntent()
    data class OnDailyChallengeEndGameShow(
        val levelModel: LevelModel,
        val dailyChallengeModel: DailyChallengeModel
    ) : EndGameIntent()

    data class OnContinueClick(val levelModel: LevelModel) : EndGameIntent()
    object OnMenuClick : EndGameIntent()
    object OnUnlockNewCardClick : EndGameIntent()
    object OnWatchAdClick : EndGameIntent()
    object OnAdReward : EndGameIntent()
    data class OnAdDismiss(val wasRewardGranted: Boolean) : EndGameIntent()
    object OnShareClick : EndGameIntent()
    object OnFreeCoinsClick : EndGameIntent()
    object OnScreenResume : EndGameIntent()
    object OnDailyChallengeStarsAnimationFinished : EndGameIntent()
    object OnDailyChallengeShareClick : EndGameIntent()
    object OnLevelComplete : EndGameIntent()
    object OnRewardCoinsReady : EndGameIntent()
}
