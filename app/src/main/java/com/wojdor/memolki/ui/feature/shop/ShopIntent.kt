package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.ui.base.UiIntent

sealed class ShopIntent : UiIntent {
    object OnWatchAdClick : ShopIntent()
    object OnAdReward : ShopIntent()
    object OnBuyCoinsSmallAmountClick : ShopIntent()
    object OnBuyCoinsBigAmountClick : ShopIntent()
    object OnBuyAllCardsClick : ShopIntent()
    data class OnAdDismiss(val wasRewardGranted: Boolean) : ShopIntent()
    object OnDailyRewardCollectClick : ShopIntent()
}
