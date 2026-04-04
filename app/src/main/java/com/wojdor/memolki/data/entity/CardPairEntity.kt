package com.wojdor.memolki.data.entity

data class CardPairEntity(
    val id: String,
    val pair: Pair<CardEntity, CardEntity>,
    val addedEpochDay: Long = 20439L // 2025-12-17
)
