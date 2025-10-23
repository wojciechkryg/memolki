package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ShopMenuModel(@field:StringRes val textId: Int) : Parcelable {
    data class WatchAd(
        val isAdAvailable: Boolean,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_obtain)

    data class BuyCoinsSmallAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy)

    data class BuyCoinsBigAmount(
        val formattedPrice: String,
        val coinsToGrant: Long
    ) : ShopMenuModel(R.string.shop_buy)

    data class BuyAllCards(val formattedPrice: String) : ShopMenuModel(R.string.shop_unlock_all_cards)
}
