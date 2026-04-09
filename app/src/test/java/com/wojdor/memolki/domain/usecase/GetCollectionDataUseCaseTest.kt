package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetCollectionDataUseCaseTest : AppTest() {

    @Inject
    lateinit var getUnlockedCardPairsUseCase: GetUnlockedCardPairsUseCase

    @Inject
    lateinit var getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase

    @Inject
    lateinit var calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase

    @Inject
    lateinit var getUnlockedCardPairsFromAdsCountUseCase: GetUnlockedCardPairsFromAdsCountUseCase

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: GetCollectionDataUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetCollectionDataUseCase(
            testDispatcher,
            getUnlockedCardPairsUseCase,
            getAllCardPairsCountUseCase,
            calculateNextCardPairCostUseCase,
            getUnlockedCardPairsFromAdsCountUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
