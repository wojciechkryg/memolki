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
    fun `when no cards are unlocked then calculate initial cost`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(11), result)
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
        assertEquals(Result.success(56), result)
    }

    @Test
    fun `when unlocking 60th card of 60 total then cost is smooth`() = runTest {
        // when
        val result = createSutForLargeCollection(totalPairs = 60, unlockedCount = 59)().first()

        // then
        assertEquals(Result.success(230), result)
    }

    @Test
    fun `when unlocking 80th card of 80 total then cost is smooth`() = runTest {
        // when
        val result = createSutForLargeCollection(totalPairs = 80, unlockedCount = 79)().first()

        // then
        assertEquals(Result.success(243), result)
    }

    @Test
    fun `when unlocking 100th card of 100 total then cost is smooth`() = runTest {
        // when
        val result = createSutForLargeCollection(totalPairs = 100, unlockedCount = 99)().first()

        // then
        assertEquals(Result.success(252), result)
    }

    @Test
    fun `when unlocking 120th card of 120 total then cost is smooth`() = runTest {
        // when
        val result = createSutForLargeCollection(totalPairs = 120, unlockedCount = 119)().first()

        // then
        assertEquals(Result.success(258), result)
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

    private fun createSutForLargeCollection(
        totalPairs: Int,
        unlockedCount: Int
    ): CalculateNextCardPairCostUseCase {
        val allLevelsUnlocked = listOf(
            LevelModel.Grid2x3(isUnlocked = true),
            LevelModel.Grid3x4(isUnlocked = true),
            LevelModel.Grid4x4(isUnlocked = true),
            LevelModel.Grid4x5(isUnlocked = true),
            LevelModel.Grid4x6(isUnlocked = true),
            LevelModel.Grid5x6(isUnlocked = true)
        )
        val mockGetLevels = mockk<GetLevelsUseCase>()
        every { mockGetLevels() } returns flowOf(Result.success(allLevelsUnlocked))

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
