@file:UseSerializers(StringResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.daily_reward_day
import com.wojdor.memolki.shared.resources.shop_buy
import com.wojdor.memolki.shared.resources.shop_obtain
import com.wojdor.memolki.shared.resources.shop_unlock_all_cards
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class ShopMenuModel(
    val textId: StringResource
) {
    @Serializable
    data class DailyReward(
        override val isAvailable: Boolean,
        val streakDay: Int,
        val coinsToGrant: Long
    ) : ShopMenuModel(Res.string.daily_reward_day)

    @Serializable
    data class WatchAd(
        override val isAvailable: Boolean,
        val coinsToGrant: Long
    ) : ShopMenuModel(Res.string.shop_obtain)

    @Serializable
    data class BuyCoinsSmallAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(Res.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    @Serializable
    data class BuyCoinsBigAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(Res.string.shop_buy) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    @Serializable
    data class BuyAllCards(
        val formattedPrice: String
    ) : ShopMenuModel(Res.string.shop_unlock_all_cards) {

        override val isAvailable: Boolean
            get() = formattedPrice.isNotBlank()
    }

    abstract val isAvailable: Boolean
}
