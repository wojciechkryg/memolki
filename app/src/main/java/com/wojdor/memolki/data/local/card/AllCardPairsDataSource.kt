package com.wojdor.memolki.data.local.card

import com.wojdor.memolki.data.entity.CardPairEntity

fun interface AllCardPairsDataSource {
    fun getAllCardPairs(): List<CardPairEntity>

    fun getCardPairById(pairId: String): CardPairEntity? {
        return getAllCardPairs().find { it.id == pairId }
    }
}
