package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.local.card.AllCardPairsDataSource
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val allCardPairsDataSource: AllCardPairsDataSource,
    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
) {

    fun getAllCardPairs() = allCardPairsDataSource.getAllCardPairs().toModel()

    suspend fun getUnlockedCardPairIds() = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()

    suspend fun getRandomUnlockedCardPairIds(count: Int) =
        unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()
            .shuffled()
            .take(count)

    fun getCardPairById(pairId: String) = allCardPairsDataSource.getCardPairById(pairId)?.toModel()
}
