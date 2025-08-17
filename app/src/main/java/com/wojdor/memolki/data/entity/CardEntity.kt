package com.wojdor.memolki.data.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class CardEntity {
    
    abstract val textRes: Int

    data class Text(
        @StringRes override val textRes: Int,
    ) : CardEntity()

    data class Image(
        @StringRes override val textRes: Int,
        @DrawableRes val imageRes: Int
    ) : CardEntity()
}
