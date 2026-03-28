package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ShopMenuModel(@field:StringRes val textId: Int) : Parcelable {
    data class DailyReward(
        override val isAvailable: Boolean,
        val streakDay: Int,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.daily_reward_day)

    data class WatchAd(
        override val isAvailable: Boolean,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_obtain)

    data class BuyCoinsSmallAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    data class BuyCoinsBigAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    data class BuyAllCards(
        val formattedPrice: String
    ) : ShopMenuModel(R.string.shop_unlock_all_cards) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    abstract val isAvailable: Boolean
}
