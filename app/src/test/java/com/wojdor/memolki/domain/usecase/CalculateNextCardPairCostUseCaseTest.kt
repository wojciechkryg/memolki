package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase.Companion.NO_MORE_CARDS
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CalculateNextCardPairCostUseCaseTest : AppTest() {

    @Inject
    lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase

    @Inject
    lateinit var getLevelsUseCase: GetLevelsUseCase

    private lateinit var sut: CalculateNextCardPairCostUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CalculateNextCardPairCostUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase,
            getLevelsUseCase,
            cardRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when 5 cards unlocked with 2x3 board then cost is low`() = runTest {
        // given: 5 unlocked, biggest level 2x3 (3 pairs) → 5 * 3 / 5 = 3
        val result = createSut(unlockedCount = 5, biggestLevel = LevelModel.Grid2x3())().first()

        // then
        assertEquals(Result.success(3), result)
    }

    @Test
    fun `when 10 cards unlocked with 4x5 board then cost scales linearly`() = runTest {
        // given: 10 unlocked, biggest level 4x5 (10 pairs) → 10 * 10 / 5 = 20
        val result = createSut(unlockedCount = 10, biggestLevel = LevelModel.Grid4x5())().first()

        // then
        assertEquals(Result.success(20), result)
    }

    @Test
    fun `when 20 cards unlocked with 5x6 board then cost is higher`() = runTest {
        // given: 20 unlocked, biggest level 5x6 (15 pairs) → 20 * 15 / 5 = 60
        val result = createSut(unlockedCount = 20, biggestLevel = LevelModel.Grid5x6())().first()

        // then
        assertEquals(Result.success(60), result)
    }

    @Test
    fun `when same unlock count then cost is same regardless of total cards`() = runTest {
        // given: 20 unlocked, 5x6 board — cost should be identical for 60 and 80 total
        val result60 = createSut(unlockedCount = 20, totalPairs = 60)().first()
        val result80 = createSut(unlockedCount = 20, totalPairs = 80)().first()

        // then
        assertEquals(result60, result80)
    }

    @Test
    fun `when all cards are unlocked then return no more cards`() = runTest {
        // given
        val result = createSut(unlockedCount = 60, totalPairs = 60)().first()

        // then
        assertTrue(result.isSuccess)
        assertEquals(NO_MORE_CARDS, result.getOrThrow())
    }

    @Test
    fun `when cost is very low then coerce to minimum`() = runTest {
        // given: 1 unlocked, 2x3 (3 pairs) → 1 * 3 / 5 = 0 → coerced to 1
        val result = createSut(unlockedCount = 1, biggestLevel = LevelModel.Grid2x3())().first()

        // then
        assertEquals(Result.success(1), result)
    }

    private fun createSut(
        unlockedCount: Int,
        totalPairs: Int = 60,
        biggestLevel: LevelModel = LevelModel.Grid5x6(isUnlocked = true)
    ): CalculateNextCardPairCostUseCase {
        val biggestSize = biggestLevel.columns * biggestLevel.rows
        val allLevelsUpTo = listOf(
            LevelModel.Grid2x3(isUnlocked = 6 <= biggestSize),
            LevelModel.Grid3x4(isUnlocked = 12 <= biggestSize),
            LevelModel.Grid4x4(isUnlocked = 16 <= biggestSize),
            LevelModel.Grid4x5(isUnlocked = 20 <= biggestSize),
            LevelModel.Grid4x6(isUnlocked = 24 <= biggestSize),
            LevelModel.Grid5x6(isUnlocked = 30 <= biggestSize)
        )

        val mockGetLevels = mockk<GetLevelsUseCase>()
        every { mockGetLevels() } returns flowOf(Result.success(allLevelsUpTo))

        val mockGetUnlockedCount = mockk<GetUnlockedCardPairsCountUseCase>()
        every { mockGetUnlockedCount() } returns flowOf(Result.success(unlockedCount))

        val mockCardRepository = mockk<CardRepository>()
        every { mockCardRepository.getAllCardPairs() } returns List(totalPairs) { mockk<CardPairModel>() }

        return CalculateNextCardPairCostUseCase(
            testDispatcher,
            mockGetUnlockedCount,
            mockGetLevels,
            mockCardRepository
        )
    }
}
