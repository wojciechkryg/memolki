package com.wojdor.memolki.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class CardModel {

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
