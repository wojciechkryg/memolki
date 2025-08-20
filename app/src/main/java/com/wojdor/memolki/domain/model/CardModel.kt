package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CardModel : Parcelable {

    abstract val pairId: String
    abstract val textRes: Int

    data class Text(
        override val pairId: String,
        @StringRes override val textRes: Int,
    ) : CardModel()

    data class Image(
        override val pairId: String,
        @StringRes override val textRes: Int,
        @DrawableRes val imageRes: Int
    ) : CardModel()
}
