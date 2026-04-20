package com.wojdor.memolki.data.entity

sealed class CardEntity {

    abstract val id: String
    abstract val textRes: Int

    data class Text(
        override val id: String,
        override val textRes: Int
    ) : CardEntity()

    data class Image(
        override val id: String,
        override val textRes: Int,
        val imageRes: Int
    ) : CardEntity()
}
