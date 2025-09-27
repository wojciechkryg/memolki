package com.wojdor.memolki.ui.ads

import android.content.Context
import com.wojdor.memolki.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllRewardedAds @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    val endGameCoinsAd = RewardedAd(context, R.string.ad_mob_end_game_coins)
    val collectionCardPairAd = RewardedAd(context, R.string.ad_mob_collection_card_pair)
    val shopCoinsAd = RewardedAd(context, R.string.ad_mob_shop_coins)

    fun loadAllAds() {
        endGameCoinsAd.load()
        collectionCardPairAd.load()
        shopCoinsAd.load()
    }
}
