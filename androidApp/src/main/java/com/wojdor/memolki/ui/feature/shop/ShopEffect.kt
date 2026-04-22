package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.util.billing.BillingProduct

sealed class ShopEffect : UiEffect {
    object OpenEnableNotificationsScreen : ShopEffect()
    data class ShowAd(val rewardedAd: RewardedAd) : ShopEffect()
    data class LaunchBilling(val product: BillingProduct) : ShopEffect()

    object ShowPurchaseFailedError : ShopEffect()
    object ShowConnectionError : ShopEffect()
    data class SendTotalCoinsScore(val totalCoins: Long) : ShopEffect()
}
