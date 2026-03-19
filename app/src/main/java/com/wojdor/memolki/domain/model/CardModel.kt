package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CardModel : Parcelable {

    abstract val id: String
    abstract val pairId: String
    abstract val textRes: Int
    abstract val isFlippedFront: Boolean
    abstract val isPairMatched: Boolean
    abstract val isMatchAnimating: Boolean
    abstract val isMismatchShaking: Boolean

    object Empty : CardModel() {
        @IgnoredOnParcel
        override val id: String = ""

        @IgnoredOnParcel
        override val pairId: String = ""

        @IgnoredOnParcel
        override val textRes: Int = R.string.empty

        @IgnoredOnParcel
        override val isFlippedFront: Boolean = false

        @IgnoredOnParcel
        override val isPairMatched: Boolean = false

        @IgnoredOnParcel
        override val isMatchAnimating: Boolean = false

        @IgnoredOnParcel
        override val isMismatchShaking: Boolean = false
    }

    data class Text(
        override val id: String,
        override val pairId: String,
        @field:StringRes override val textRes: Int,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMismatchShaking: Boolean = false
    ) : CardModel()

    data class Image(
        override val id: String,
        override val pairId: String,
        @field:StringRes override val textRes: Int,
        @field:DrawableRes val imageRes: Int,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMismatchShaking: Boolean = false
    ) : CardModel()
}
