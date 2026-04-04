package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.domain.model.CardPairModel
import javax.inject.Inject
import kotlin.random.Random

class CardRepository @Inject constructor(
    private val allCardPairsDataSource: AllCardPairsDataSource,
    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource,
    private val random: Random
) {

    fun getAllCardPairs() = allCardPairsDataSource.getAllCardPairs().toModel()

    suspend fun getUnlockedCardPairs(): List<CardPairModel> {
        val unlockedCardPairIds = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()
        val allCardPairs = allCardPairsDataSource.getAllCardPairs()
        return unlockedCardPairIds.mapNotNull { id ->
            allCardPairs.firstOrNull { it.id == id }?.toModel()
        }
    }

    suspend fun getLockedCardPairs(): List<CardPairModel> {
        val unlockedCardPairIds = unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()
        val allCardPairs = allCardPairsDataSource.getAllCardPairs()
        return allCardPairs.filter { it.id !in unlockedCardPairIds }.toModel()
    }

    suspend fun getRandomUnlockedCardPairIds(count: Int) =
        unlockedCardPairsLocalDataSource.getUnlockedCardPairIds()
            .shuffled(random)
            .take(count)

    suspend fun addUnlockedCardPairId(unlockedCardPairId: String) {
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId(unlockedCardPairId)
    }

    suspend fun unlockAllCardPairs() {
        getLockedCardPairs().forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it.first.pairId)
        }
    }

    fun getCardPairById(pairId: String) = allCardPairsDataSource.getCardPairById(pairId)?.toModel()

}
