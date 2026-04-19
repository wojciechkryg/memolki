package com.wojdor.memolki.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

@Serializable
sealed class CardModel {

    abstract val id: String
    abstract val pairId: String
    abstract val textRes: Int
    abstract val isFlippedFront: Boolean
    abstract val isPairMatched: Boolean
    abstract val isMatchAnimating: Boolean
    abstract val isMistakeShaking: Boolean

    fun copyState(
        isFlippedFront: Boolean = this.isFlippedFront,
        isPairMatched: Boolean = this.isPairMatched,
        isMatchAnimating: Boolean = this.isMatchAnimating,
        isMistakeShaking: Boolean = this.isMistakeShaking
    ): CardModel = when (this) {
        is Text -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMistakeShaking = isMistakeShaking
        )

        is Image -> copy(
            isFlippedFront = isFlippedFront,
            isPairMatched = isPairMatched,
            isMatchAnimating = isMatchAnimating,
            isMistakeShaking = isMistakeShaking
        )

        Empty -> this
    }

    @Serializable
    object Empty : CardModel() {
        override val id: String = ""
        override val pairId: String = ""
        override val textRes: Int = R.string.empty
        override val isFlippedFront: Boolean = false
        override val isPairMatched: Boolean = false
        override val isMatchAnimating: Boolean = false
        override val isMistakeShaking: Boolean = false
    }

    @Serializable
    data class Text(
        override val id: String,
        override val pairId: String,
        @field:StringRes override val textRes: Int,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMistakeShaking: Boolean = false
    ) : CardModel()

    @Serializable
    data class Image(
        override val id: String,
        override val pairId: String,
        @field:StringRes override val textRes: Int,
        @field:DrawableRes val imageRes: Int,
        override val isFlippedFront: Boolean = false,
        override val isPairMatched: Boolean = false,
        override val isMatchAnimating: Boolean = false,
        override val isMistakeShaking: Boolean = false
    ) : CardModel()
}
