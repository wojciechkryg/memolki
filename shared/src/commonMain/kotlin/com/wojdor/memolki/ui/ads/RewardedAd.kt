package com.wojdor.memolki.ui.ads

interface RewardedAd {
    val isLoaded: Boolean

    fun loadAndNotify(
        wasRewardGranted: Boolean = false,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    )
}
