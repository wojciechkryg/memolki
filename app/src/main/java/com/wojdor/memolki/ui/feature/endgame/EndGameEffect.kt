package com.wojdor.memolki.ui.feature.endgame

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect

sealed class EndGameEffect : UiEffect {
    data class OpenGameScreen(val levelModel: LevelModel) : EndGameEffect()
    object OpenMenuScreen : EndGameEffect()
    data class ShowAd(val rewardedAd: RewardedAd) : EndGameEffect()
    data class RequestReview(
        val reviewManager: ReviewManager,
        val reviewInfo: ReviewInfo
    ) : EndGameEffect()
}
