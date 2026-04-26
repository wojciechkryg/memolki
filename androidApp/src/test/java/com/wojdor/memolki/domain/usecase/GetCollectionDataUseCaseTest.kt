package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetCollectionDataUseCaseTest : AppTest() {

    private val getUnlockedCardPairsUseCase: GetUnlockedCardPairsUseCase by inject()

    private val getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase by inject()

    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase by inject()

    private val getUnlockedCardPairsFromAdsCountUseCase: GetUnlockedCardPairsFromAdsCountUseCase by inject()

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetCollectionDataUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when called then returns collection data with all fields`() = runTest {
        sut().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            val data = result.getOrThrow()
            assertEquals(5, data.unlockedCardPairs.size)
            assertEquals(10, data.allCardPairsCount)
            assertTrue(data.nextCardPairCost >= CalculateNextCardPairCostUseCase.MINIMUM_CARD_PAIR_COST)
            assertEquals(0, data.unlockedCardPairsFromAdsCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when ads unlocked cards count changes then returns updated count`() = runTest {
        // given
        userRepository.incrementUnlockedCardPairsFromAdsCount()
        userRepository.incrementUnlockedCardPairsFromAdsCount()

        // when
        sut().test {
            val result = awaitItem()

            // then
            assertTrue(result.isSuccess)
            assertEquals(2, result.getOrThrow().unlockedCardPairsFromAdsCount)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
