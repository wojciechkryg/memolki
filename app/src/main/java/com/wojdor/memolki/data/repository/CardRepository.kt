package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.source.card.local.CardPairsDataSource
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardPairsDataSource: CardPairsDataSource
) {

    fun getAllCardPairs() = cardPairsDataSource.getCardPairs().toModel()
}
