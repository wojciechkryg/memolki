package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.data.entity.CardPairEntity

fun interface AllCardPairsDataSource {
    fun getAllCardPairs(): LinkedHashSet<CardPairEntity>

    fun getCardPairById(pairId: String): CardPairEntity? {
        return getAllCardPairs().find { it.id == pairId }
    }
}
