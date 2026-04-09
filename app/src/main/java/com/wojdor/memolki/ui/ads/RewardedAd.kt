package com.wojdor.memolki.ui.ads

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd as GoogleRewardedAd

class RewardedAd(
    private val context: Context,
    @param:StringRes private val adUnitRes: Int
) {

    private var rewardedAd: GoogleRewardedAd? = null

    val isLoaded: Boolean
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
                    rewardedAd = ad
                    onLoaded()
                }
            })
    }

    fun loadAndNotify(
        wasRewardGranted: Boolean = false,
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

    fun show(
        activity: Activity,
        onGrantReward: () -> Unit,
        onAdDismiss: (wasRewardGranted: Boolean) -> Unit = {}
    ) {
        var wasRewardGranted = false
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
