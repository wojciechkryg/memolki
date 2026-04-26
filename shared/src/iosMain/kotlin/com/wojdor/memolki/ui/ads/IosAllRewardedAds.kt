package com.wojdor.memolki.ui.ads

// TODO(kmp-ios): replace with a real AdMob (or alternative) impl when iOS ads ship.
class IosAllRewardedAds : AllRewardedAds {
    override val endGameCoinsAd: RewardedAd = IosRewardedAd
    override val collectionCardPairAd: RewardedAd = IosRewardedAd
    override val shopCoinsAd: RewardedAd = IosRewardedAd

    override fun loadAllAds() = Unit
}

private object IosRewardedAd : RewardedAd {
    override val isLoaded: Boolean = false

    override fun loadAndNotify(
        wasRewardGranted: Boolean,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    ) {
        onAvailabilityChanged(false)
    }

    override fun show(
        onGrantReward: () -> Unit,
        onAdDismiss: (wasRewardGranted: Boolean) -> Unit
    ) = Unit
}
