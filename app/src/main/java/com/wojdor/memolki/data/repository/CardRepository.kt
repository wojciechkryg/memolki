package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.source.card.local.AllCardPairsDataSource
import com.wojdor.memolki.data.source.card.local.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.util.extension.toLinkedSet
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val allCardPairsDataSource: AllCardPairsDataSource,
    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
) {

    fun getAllCardPairs() = allCardPairsDataSource.getAllCardPairs().toModel()

    fun getUnlockedCardPairIds() = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()

    fun getRandomUnlockedCardPairIds(count: Int) =
        unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()
            .shuffled()
            .take(count)
            .toLinkedSet()

    fun getCardPairById(pairId: String) = allCardPairsDataSource.getCardPairById(pairId)?.toModel()
}
