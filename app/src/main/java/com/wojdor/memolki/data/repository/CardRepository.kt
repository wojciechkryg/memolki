package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.source.card.local.CardPairsLocalDataSource
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardPairsLocalDataSource: CardPairsLocalDataSource
) {

    fun getCardPairs() = cardPairsLocalDataSource.getCardPairs().toModel()
}
