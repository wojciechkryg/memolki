package com.wojdor.memolki.data.entity

data class CardPairEntity(
    val id: String,
    val pair: Pair<CardEntity, CardEntity>
)
