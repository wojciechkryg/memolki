package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.ui.base.UiIntent

sealed class ShopIntent : UiIntent {

    object OnRewardCoinsWithAdClick : ShopIntent()

    object OnAdReward : ShopIntent()

    data class OnAdDismiss(val wasRewardGranted: Boolean) : ShopIntent()
}

