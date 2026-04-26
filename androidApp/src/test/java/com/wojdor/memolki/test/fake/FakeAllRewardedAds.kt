package com.wojdor.memolki.test.fake

import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.RewardedAd

class FakeRewardedAd(override val isLoaded: Boolean = false) : RewardedAd {
    var loadCount: Int = 0
        private set
    var showCount: Int = 0
        private set

    override fun loadAndNotify(
        wasRewardGranted: Boolean,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    ) {
        loadCount++
        onAvailabilityChanged(false)
    }

    override fun show(
        onGrantReward: () -> Unit,
        onAdDismiss: (wasRewardGranted: Boolean) -> Unit
    ) {
        showCount++
    }
}

class FakeAllRewardedAds : AllRewardedAds {
    override val endGameCoinsAd: RewardedAd = FakeRewardedAd()
    override val collectionCardPairAd: RewardedAd = FakeRewardedAd()
    override val shopCoinsAd: RewardedAd = FakeRewardedAd()

    var loadAllAdsCount: Int = 0
        private set

    override fun loadAllAds() {
        loadAllAdsCount++
    }
}
