package com.wojdor.memolki.ui.ads

interface AllRewardedAds {
    val endGameCoinsAd: RewardedAd
    val collectionCardPairAd: RewardedAd
    val shopCoinsAd: RewardedAd

    fun loadAllAds()
}
