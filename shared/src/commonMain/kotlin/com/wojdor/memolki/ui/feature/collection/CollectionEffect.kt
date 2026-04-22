package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect

sealed class CollectionEffect : UiEffect {
    object OpenShopScreen : CollectionEffect()
    object OpenEnableNotificationsScreen : CollectionEffect()
    data class OpenCardPairDetailsScreen(val cardPairModel: CardPairModel) : CollectionEffect()
    data class ShowAd(val rewardedAd: RewardedAd) : CollectionEffect()
}
