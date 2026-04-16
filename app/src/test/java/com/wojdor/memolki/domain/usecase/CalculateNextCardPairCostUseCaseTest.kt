package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase.Companion.NO_MORE_CARDS
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var getBoardsUseCase: GetBoardsUseCase

    private lateinit var sut: CalculateNextCardPairCostUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CalculateNextCardPairCostUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase,
            getBoardsUseCase,
            cardRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when 5 cards unlocked with 2x3 board then cost is low`() = runTest {
        // when
        createSut(unlockedCount = 5, biggestLevel = BoardModel.Grid2x3())().test {
            // then
            assertEquals(Result.success(1), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when 10 cards unlocked with 4x5 board then cost scales linearly`() = runTest {
        // when
        createSut(unlockedCount = 10, biggestLevel = BoardModel.Grid4x5())().test {
            // then
            assertEquals(Result.success(20), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when 20 cards unlocked with 5x6 board then cost is higher`() = runTest {
        // when
        createSut(unlockedCount = 20, biggestLevel = BoardModel.Grid5x6())().test {
            // then
            assertEquals(Result.success(60), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when same unlock count then cost is same regardless of total cards`() = runTest {
        // when
        createSut(unlockedCount = 20, totalPairs = 60)().test {
            // then
            assertEquals(Result.success(60), awaitItem())
            awaitComplete()
        }
        createSut(unlockedCount = 20, totalPairs = 80)().test {
            // then
            assertEquals(Result.success(60), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when all cards are unlocked then return no more cards`() = runTest {
        // when
        createSut(unlockedCount = 60, totalPairs = 60)().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(NO_MORE_CARDS, result.getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun `when more cards unlocked than total then return no more cards`() = runTest {
        // when
        createSut(unlockedCount = 70, totalPairs = 60)().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(NO_MORE_CARDS, result.getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun `when cost is very low then coerce to minimum`() = runTest {
        // when
        createSut(unlockedCount = 1, biggestLevel = BoardModel.Grid2x3())().test {
            // then
            assertEquals(Result.success(1), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when no boards are unlocked but unlocked count is above initial then cost is minimum`() = runTest {
        // given
        val allLocked = listOf(
            BoardModel.Grid2x3(isUnlocked = false),
            BoardModel.Grid3x4(isUnlocked = false),
            BoardModel.Grid4x4(isUnlocked = false),
            BoardModel.Grid4x5(isUnlocked = false),
            BoardModel.Grid4x6(isUnlocked = false),
            BoardModel.Grid5x6(isUnlocked = false)
        )
        val mockGetLevels = mockk<GetBoardsUseCase>()
        every { mockGetLevels() } returns flowOf(Result.success(allLocked))
        val mockGetUnlockedCount = mockk<GetUnlockedCardPairsCountUseCase>()
        every { mockGetUnlockedCount() } returns flowOf(Result.success(6))
        val mockCardRepository = mockk<CardRepository>()
        every { mockCardRepository.getAllCardPairs() } returns List(60) { mockk<CardPairModel>() }
        val sut = CalculateNextCardPairCostUseCase(
            testDispatcher,
            mockGetUnlockedCount,
            mockGetLevels,
            mockCardRepository
        )

        // when
        sut().test {
            // then
            assertEquals(Result.success(1), awaitItem())
            awaitComplete()
        }
    }

    private fun createSut(
        unlockedCount: Int,
        totalPairs: Int = 60,
        biggestLevel: BoardModel = BoardModel.Grid5x6(isUnlocked = true)
    ): CalculateNextCardPairCostUseCase {
        val biggestSize = biggestLevel.columns * biggestLevel.rows
        val allLevelsUpTo = listOf(
            BoardModel.Grid2x3(isUnlocked = 6 <= biggestSize),
            BoardModel.Grid3x4(isUnlocked = 12 <= biggestSize),
            BoardModel.Grid4x4(isUnlocked = 16 <= biggestSize),
            BoardModel.Grid4x5(isUnlocked = 20 <= biggestSize),
            BoardModel.Grid4x6(isUnlocked = 24 <= biggestSize),
            BoardModel.Grid5x6(isUnlocked = 30 <= biggestSize)
        )

        val mockGetLevels = mockk<GetBoardsUseCase>()
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
