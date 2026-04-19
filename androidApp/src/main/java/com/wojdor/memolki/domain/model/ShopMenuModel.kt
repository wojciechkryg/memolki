package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

// TODO(compose-resources): move to commonMain once Phase 13 lets us replace R.string.* in subclass super-constructors with Res.*
@Serializable
sealed class ShopMenuModel(@field:StringRes val textId: Int) {
    @Serializable
    data class DailyReward(
        override val isAvailable: Boolean,
        val streakDay: Int,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.daily_reward_day)

    @Serializable
    data class WatchAd(
        override val isAvailable: Boolean,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_obtain)

    @Serializable
    data class BuyCoinsSmallAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    @Serializable
    data class BuyCoinsBigAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    @Serializable
    data class BuyAllCards(
        val formattedPrice: String
    ) : ShopMenuModel(R.string.shop_unlock_all_cards) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    abstract val isAvailable: Boolean
}
