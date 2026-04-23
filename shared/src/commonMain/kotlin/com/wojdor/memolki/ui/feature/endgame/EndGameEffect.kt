package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination

sealed class EndGameEffect : UiEffect {
    data class OpenGameScreen(val boardModel: BoardModel) : EndGameEffect()
    object OpenMenuScreen : EndGameEffect()
    object OpenCollectionScreen : EndGameEffect()
    data class OpenEnableNotificationsScreen(
        val destination: EnableNotificationDestination,
        val boardModel: BoardModel?
    ) : EndGameEffect()

    data class ShowAd(val rewardedAd: RewardedAd) : EndGameEffect()

    data class SendTotalCoinsScore(val totalCoins: Long) : EndGameEffect()

    data class Share(val text: String) : EndGameEffect()
    data class ShareDailyChallenge(val text: String) : EndGameEffect()
}
