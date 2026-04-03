package com.wojdor.memolki.ui.feature.endgame

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination

sealed class EndGameEffect : UiEffect {
    data class OpenGameScreen(val levelModel: LevelModel) : EndGameEffect()
    object OpenMenuScreen : EndGameEffect()
    object OpenCollectionScreen : EndGameEffect()
    data class OpenEnableNotificationsScreen(
        val destination: EnableNotificationDestination,
        val levelModel: LevelModel?
    ) : EndGameEffect()

    data class ShowAd(val rewardedAd: RewardedAd) : EndGameEffect()
    data class RequestReview(
        val reviewManager: ReviewManager,
        val reviewInfo: ReviewInfo
    ) : EndGameEffect()

    data class SendTotalCoinsScore(val totalCoins: Long) : EndGameEffect()

    object Share : EndGameEffect()
    object OpenShopScreen : EndGameEffect()
    data class ShareDailyChallenge(val text: String) : EndGameEffect()
}
