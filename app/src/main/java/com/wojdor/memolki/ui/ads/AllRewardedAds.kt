package com.wojdor.memolki.ui.ads

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.util.analytics.Analytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllRewardedAds @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val analytics: Analytics
) {
    val endGameCoinsAd = RewardedAd(
        context,
        R.string.ad_mob_end_game_coins,
        onPaidEvent = ::logAdImpression
    )
    val collectionCardPairAd = RewardedAd(
        context,
        R.string.ad_mob_collection_card_pair,
        onPaidEvent = ::logAdImpression
    )
    val shopCoinsAd = RewardedAd(
        context,
        R.string.ad_mob_shop_coins,
        onPaidEvent = ::logAdImpression
    )

    fun loadAllAds() {
        endGameCoinsAd.load()
        collectionCardPairAd.load()
        shopCoinsAd.load()
    }

    private fun logAdImpression(valueMicros: Long, currencyCode: String, adUnitId: String) {
        analytics.logAdImpression(
            valueMicros = valueMicros,
            currencyCode = currencyCode,
            adFormat = AD_FORMAT_REWARDED,
            adUnitName = adUnitId
        )
    }

    companion object {
        private const val AD_FORMAT_REWARDED = "Rewarded"
    }
}
