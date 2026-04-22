package com.wojdor.memolki.data.entity

import org.jetbrains.compose.resources.StringResource

sealed class CardEntity {

    abstract val id: String
    abstract val textRes: StringResource

    data class Text(
        override val id: String,
        override val textRes: StringResource
    ) : CardEntity()

    data class Image(
        override val id: String,
        override val textRes: StringResource,
        val imageRes: Int
    ) : CardEntity()
}
