package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase.Companion.NO_MORE_CARDS
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class CalculateNextCardPairCostUseCaseTest : AppTest() {

    private lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource
    private lateinit var cardRepository: CardRepository
    private lateinit var sut: CalculateNextCardPairCostUseCase

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        unlockedCardPairsLocalDataSource =
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        cardRepository =
            CardRepository(MockAllCardPairsDataSource, unlockedCardPairsLocalDataSource)
        val getUnlockedCardPairsCountUseCase =
            GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository)
        sut = CalculateNextCardPairCostUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase,
            GetLevelsUseCase(testDispatcher, getUnlockedCardPairsCountUseCase),
            cardRepository
        )
    }

    @Test
    fun `when no cards are unlocked then calculate initial cost`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(9), result)
    }

    @Test
    fun `when some cards are unlocked then calculate higher cost`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("peach")

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(169), result)
    }

    @Test
    fun `when all cards are unlocked then return no more cards`() = runTest {
        // given
        cardRepository.getAllCardPairs().forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId((it.first.pairId))
        }

        // when
        val result = sut().first()

        // then
        assertTrue(result.isSuccess)
        assertEquals(NO_MORE_CARDS, result.getOrThrow())
    }
}
