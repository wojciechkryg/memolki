package com.wojdor.memolki.ui.ads

// TODO(kmp-ios): replace with a real AdMob (or alternative) impl when iOS ads ship.
class NoopAllRewardedAds : AllRewardedAds {
    override val endGameCoinsAd: RewardedAd = NoopRewardedAd
    override val collectionCardPairAd: RewardedAd = NoopRewardedAd
    override val shopCoinsAd: RewardedAd = NoopRewardedAd

    override fun loadAllAds() = Unit
}

private object NoopRewardedAd : RewardedAd {
    override val isLoaded: Boolean = false

    override fun loadAndNotify(
        wasRewardGranted: Boolean,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    ) {
        onAvailabilityChanged(false)
    }
}
