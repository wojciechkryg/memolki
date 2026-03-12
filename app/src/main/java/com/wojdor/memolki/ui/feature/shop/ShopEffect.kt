package com.wojdor.memolki.ui.feature.shop

import com.android.billingclient.api.ProductDetails
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.util.billing.BillingHandler

sealed class ShopEffect : UiEffect {
    data class ShowAd(val rewardedAd: RewardedAd) : ShopEffect()
    data class LaunchBilling(
        val billingHandler: BillingHandler,
        val productDetails: ProductDetails
    ) : ShopEffect()

    object ShowPurchaseFailedError : ShopEffect()
    object ShowConnectionError : ShopEffect()
    data class SendTotalCoinsScore(
        val googlePlayGames: GooglePlayGames,
        val totalCoins: Long
    ) : ShopEffect()
}
