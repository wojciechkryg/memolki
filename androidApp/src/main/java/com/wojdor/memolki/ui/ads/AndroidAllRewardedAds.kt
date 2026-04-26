package com.wojdor.memolki.ui.ads

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.provider.ActivityProvider

open class AndroidAllRewardedAds(
    private val context: Context,
    private val activityProvider: ActivityProvider,
    private val analytics: Analytics
) : AllRewardedAds {
    override val endGameCoinsAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
        activityProvider,
        R.string.ad_mob_end_game_coins,
        onPaidEvent = ::logAdImpression
    )
    override val collectionCardPairAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
        activityProvider,
        R.string.ad_mob_collection_card_pair,
        onPaidEvent = ::logAdImpression
    )
    override val shopCoinsAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
        activityProvider,
        R.string.ad_mob_shop_coins,
        onPaidEvent = ::logAdImpression
    )

    override fun loadAllAds() {
        endGameCoinsAd.load()
        collectionCardPairAd.load()
        shopCoinsAd.load()
    }

    private fun logAdImpression(
        valueMicros: Long,
        currencyCode: String,
        adUnitId: String,
        adSource: String?
    ) {
        analytics.logAdImpression(
            valueMicros = valueMicros,
            currencyCode = currencyCode,
            adFormat = AD_FORMAT_REWARDED,
            adUnitName = adUnitId,
            adSource = adSource
        )
    }

    private companion object {
        private const val AD_FORMAT_REWARDED = "Rewarded"
    }
}
