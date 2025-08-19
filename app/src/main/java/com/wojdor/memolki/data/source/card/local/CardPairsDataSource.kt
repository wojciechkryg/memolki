package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.data.entity.CardPairEntity

fun interface CardPairsDataSource {
    fun getCardPairs(): Set<CardPairEntity>
}
