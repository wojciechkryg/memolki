package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ShopMenuModel(@field:StringRes val textId: Int) : Parcelable {
    data class WatchAd(val isAdAvailable: Boolean) : ShopMenuModel(R.string.empty)
    object BuyCoinsSmallAmount : ShopMenuModel(R.string.shop_buy)
    object BuyCoinsBigAmount : ShopMenuModel(R.string.shop_buy)
    object BuyAllCards : ShopMenuModel(R.string.shop_unlock_all_cards)
}
