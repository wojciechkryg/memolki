package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.base.UiEffect

sealed class ShopEffect : UiEffect {
    data class ShowAd(val rewardedAd: RewardedAd) : ShopEffect()
}
