package com.wojdor.memolki.ui.feature.endgame

import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.util.playgames.GooglePlayGames

sealed class EndGameEffect : UiEffect {
    data class OpenGameScreen(val boardModel: BoardModel) : EndGameEffect()
    object OpenMenuScreen : EndGameEffect()
    object OpenCollectionScreen : EndGameEffect()
    data class OpenEnableNotificationsScreen(
        val destination: EnableNotificationDestination,
        val boardModel: BoardModel?
    ) : EndGameEffect()

    data class ShowAd(val rewardedAd: RewardedAd) : EndGameEffect()
    data class RequestReview(
        val reviewManager: ReviewManager,
        val reviewInfo: ReviewInfo
    ) : EndGameEffect()

    data class SendTotalCoinsScore(
        val googlePlayGames: GooglePlayGames,
        val totalCoins: Long
    ) : EndGameEffect()

    data class Share(val text: String) : EndGameEffect()
    data class ShareDailyChallenge(val text: String) : EndGameEffect()
}
