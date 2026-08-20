package com.wojdor.memolki.ui.ads

class IosAllRewardedAds(
    private val bridge: IosRewardedAdsBridge
) : AllRewardedAds {

    override val endGameCoinsAd: RewardedAd = IosRewardedAd(AD_UNIT_END_GAME_COINS, bridge)
    override val collectionCardPairAd: RewardedAd = IosRewardedAd(AD_UNIT_COLLECTION_CARD_PAIR, bridge)
    override val shopCoinsAd: RewardedAd = IosRewardedAd(AD_UNIT_SHOP_COINS, bridge)

    override fun loadAllAds() {
        endGameCoinsAd.loadAndNotify(onAvailabilityChanged = {})
        collectionCardPairAd.loadAndNotify(onAvailabilityChanged = {})
        shopCoinsAd.loadAndNotify(onAvailabilityChanged = {})
    }

    private companion object {
        // TODO(kmp-ios): swap for real per-flavor AdMob rewarded ad unit IDs; currently Google's test unit.
        private const val TEST_REWARDED_AD_UNIT = "ca-app-pub-3940256099942544/1712485313"
        private const val AD_UNIT_END_GAME_COINS = TEST_REWARDED_AD_UNIT
        private const val AD_UNIT_COLLECTION_CARD_PAIR = TEST_REWARDED_AD_UNIT
        private const val AD_UNIT_SHOP_COINS = TEST_REWARDED_AD_UNIT
    }
}

private class IosRewardedAd(
    private val adUnitId: String,
    private val bridge: IosRewardedAdsBridge
) : RewardedAd {

    override val isLoaded: Boolean
        get() = bridge.isLoaded(adUnitId)

    override fun loadAndNotify(
        wasRewardGranted: Boolean,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    ) {
        if (isLoaded && !wasRewardGranted) {
            onAvailabilityChanged(true)
        } else {
            onAvailabilityChanged(false)
            if (!wasRewardGranted) {
                bridge.load(adUnitId, onAvailabilityChanged)
            }
        }
    }

    override fun show(
        onGrantReward: () -> Unit,
        onAdDismiss: (wasRewardGranted: Boolean) -> Unit
    ) {
        var wasRewardGranted = false
        bridge.show(
            adUnitId,
            onReward = {
                wasRewardGranted = true
                onGrantReward()
            },
            onDismiss = { onAdDismiss(wasRewardGranted) }
        )
    }
}
