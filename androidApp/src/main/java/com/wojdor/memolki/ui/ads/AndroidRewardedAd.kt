package com.wojdor.memolki.ui.ads

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wojdor.memolki.util.provider.ActivityProvider
import com.google.android.gms.ads.rewarded.RewardedAd as GoogleRewardedAd

open class AndroidRewardedAd(
    private val context: Context,
    private val activityProvider: ActivityProvider,
    @param:StringRes private val adUnitRes: Int,
    private val onPaidEvent: (valueMicros: Long, currencyCode: String, adUnitId: String, adSource: String?) -> Unit = { _, _, _, _ -> }
) : RewardedAd {

    private var rewardedAd: GoogleRewardedAd? = null

    override val isLoaded: Boolean
        get() = rewardedAd != null

    fun load(
        onLoaded: () -> Unit = {},
        onFailed: (LoadAdError) -> Unit = {}
    ) {
        val adUnitId = context.getString(adUnitRes)
        val adRequest = AdRequest.Builder().build()
        GoogleRewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    onFailed(adError)
                }

                override fun onAdLoaded(ad: GoogleRewardedAd) {
                    ad.setOnPaidEventListener { adValue ->
                        onPaidEvent(
                            adValue.valueMicros,
                            adValue.currencyCode,
                            adUnitId,
                            ad.responseInfo.loadedAdapterResponseInfo?.adSourceName
                        )
                    }
                    rewardedAd = ad
                    onLoaded()
                }
            })
    }

    override fun loadAndNotify(
        wasRewardGranted: Boolean,
        onAvailabilityChanged: (isAvailable: Boolean) -> Unit
    ) {
        if (isLoaded && !wasRewardGranted) {
            onAvailabilityChanged(true)
        } else {
            onAvailabilityChanged(false)
            if (!wasRewardGranted) {
                load(
                    onLoaded = { onAvailabilityChanged(true) },
                    onFailed = { onAvailabilityChanged(false) }
                )
            }
        }
    }

    override fun show(
        onGrantReward: () -> Unit,
        onAdDismiss: (wasRewardGranted: Boolean) -> Unit
    ) {
        var wasRewardGranted = false
        val activity = activityProvider.current ?: return
        if (!isLoaded || activity.isFinishing || activity.isDestroyed) {
            return
        }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                onAdDismiss(wasRewardGranted)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                onAdDismiss(wasRewardGranted)
            }
        }

        rewardedAd?.show(activity) {
            wasRewardGranted = true
            onGrantReward()
        }
    }
}
