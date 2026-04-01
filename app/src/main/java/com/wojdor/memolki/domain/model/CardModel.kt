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

    fun copyState(
        isFlippedFront: Boolean = this.isFlippedFront,
        isPairMatched: Boolean = this.isPairMatched,
        isMatchAnimating: Boolean = this.isMatchAnimating,
        isMismatchShaking: Boolean = this.isMismatchShaking
    ): CardModel = when (this) {
        is Text -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMismatchShaking = isMismatchShaking
        )

        is Image -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMismatchShaking = isMismatchShaking
        )

        Empty -> this
    }

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
