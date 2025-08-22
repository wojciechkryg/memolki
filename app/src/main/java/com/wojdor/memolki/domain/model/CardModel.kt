package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CardModel : Parcelable {

    abstract val pairId: String
    abstract val textRes: Int
    abstract val isFlipped: Boolean
    abstract val isMatched: Boolean

    object Empty : CardModel() {
        @IgnoredOnParcel
        override val pairId: String = ""

        @IgnoredOnParcel
        override val textRes: Int = 0

        @IgnoredOnParcel
        override val isFlipped: Boolean = false

        @IgnoredOnParcel
        override val isMatched: Boolean = false
    }

    data class Text(
        override val pairId: String,
        @StringRes override val textRes: Int,
        override val isFlipped: Boolean = false,
        override val isMatched: Boolean = false
    ) : CardModel()

    data class Image(
        override val pairId: String,
        @StringRes override val textRes: Int,
        @DrawableRes val imageRes: Int,
        override val isFlipped: Boolean = false,
        override val isMatched: Boolean = false
    ) : CardModel()
}
