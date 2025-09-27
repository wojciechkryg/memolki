package com.wojdor.memolki.ui.ads

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wojdor.memolki.util.extension.logD

class RewardedAd(
    private val context: Context,
    @param:StringRes private val adUnitRes: Int
) {

    private var rewardedAd: RewardedAd? = null

    val isLoaded: Boolean
        get() = rewardedAd != null

    fun load(
        onLoaded: () -> Unit = {},
        onFailed: (LoadAdError) -> Unit = {}
    ) {
        val adUnitId = context.getString(adUnitRes)
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logD("Ad failed to load with error: ${adError.message}")
                    rewardedAd = null
                    onFailed(adError)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    logD("Ad was loaded successfully.")
                    rewardedAd = ad
                    onLoaded()
                }
            })
    }

    fun show(
        activity: Activity,
        onUserEarnedReward: () -> Unit,
        onAdDismiss: () -> Unit = {}
    ) {
        var wasUserGrantedForWatchAd = false
        if (!isLoaded) {
            logD("Tried to show ad, but it was not loaded yet.")
            return
        }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                logD("Ad failed to show full screen content with error: ${adError.message}")
            }

            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                if (wasUserGrantedForWatchAd) {
                    logD("Ad was dismissed, but user was granted for watching ad.")
                    onUserEarnedReward()
                } else {
                    logD("Ad was dismissed.")
                    onAdDismiss()
                }
            }
        }

        rewardedAd?.show(activity) {
            logD("User earned the reward.")
            wasUserGrantedForWatchAd = true
        }
    }
}
