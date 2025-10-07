package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

@ExperimentalCoroutinesApi
class GetShuffledUnlockedCardsUseCaseTest : AppTest() {

    private lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
    private lateinit var cardRepository: CardRepository
    private lateinit var sut: GetShuffledUnlockedCardsUseCase

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        unlockedCardPairsLocalDataSource =
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        cardRepository =
            CardRepository(MockAllCardPairsDataSource, unlockedCardPairsLocalDataSource)
        sut = GetShuffledUnlockedCardsUseCase(testDispatcher, cardRepository, Random(0))
    }

    @Test
    fun `when there are unlocked cards then return shuffled list`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")

        // when
        val result = sut(LevelModel.Grid2x3()).first()

        // then
        assertEquals(6, result.getOrThrow().size)
    }
}
