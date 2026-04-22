package com.wojdor.memolki.ui.ads

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.util.analytics.Analytics

open class AndroidAllRewardedAds(
    private val context: Context,
    private val analytics: Analytics
) : AllRewardedAds {
    override val endGameCoinsAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
        R.string.ad_mob_end_game_coins,
        onPaidEvent = ::logAdImpression
    )
    override val collectionCardPairAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
        R.string.ad_mob_collection_card_pair,
        onPaidEvent = ::logAdImpression
    )
    override val shopCoinsAd: AndroidRewardedAd = AndroidRewardedAd(
        context,
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
