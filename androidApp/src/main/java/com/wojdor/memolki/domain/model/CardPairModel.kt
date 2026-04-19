package com.wojdor.memolki.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CardPairModel(
    val first: CardModel,
    val second: CardModel,
    val addedEpochDay: Long = 0L
)
