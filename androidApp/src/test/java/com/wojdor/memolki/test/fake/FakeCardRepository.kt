package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardPairModel
import kotlin.random.Random

class FakeCardRepository(
    allCardPairsDataSource: AllCardPairsDataSource,
    unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource,
    random: Random
) : CardRepository(allCardPairsDataSource, unlockedCardPairsLocalDataSource, random) {

    var allCardPairsOverride: List<CardPairModel>? = null

    override fun getAllCardPairs(): List<CardPairModel> =
        allCardPairsOverride ?: super.getAllCardPairs()
}
