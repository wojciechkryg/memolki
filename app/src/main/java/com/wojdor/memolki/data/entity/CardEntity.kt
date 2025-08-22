package com.wojdor.memolki.data.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class CardEntity {

    abstract val id: String
    abstract val textRes: Int

    data class Text(
        override val id: String,
        @StringRes override val textRes: Int
    ) : CardEntity()

    data class Image(
        override val id: String,
        @StringRes override val textRes: Int,
        @DrawableRes val imageRes: Int
    ) : CardEntity()
}
