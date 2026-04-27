package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase.Companion.NO_MORE_CARDS
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeCardRepository
import com.wojdor.memolki.test.fake.FakeGetBoardsUseCase
import com.wojdor.memolki.test.fake.FakeGetUnlockedCardPairsCountUseCase
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test
import org.koin.test.get

@ExperimentalCoroutinesApi
class CalculateNextCardPairCostUseCaseTest : AppTest() {

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
        // when
        createSut(
            unlockedCount = 6,
            totalPairs = 60,
            allLevelsLocked = true
        )().test {
            // then
            assertEquals(Result.success(1), awaitItem())
            awaitComplete()
        }
    }

    private fun createSut(
        unlockedCount: Int,
        totalPairs: Int = 60,
        biggestLevel: BoardModel = BoardModel.Grid5x6(isUnlocked = true),
        allLevelsLocked: Boolean = false
    ): CalculateNextCardPairCostUseCase {
        val biggestSize = biggestLevel.columns * biggestLevel.rows
        val allLevels = listOf(
            BoardModel.Grid2x3(isUnlocked = !allLevelsLocked && 6 <= biggestSize),
            BoardModel.Grid3x4(isUnlocked = !allLevelsLocked && 12 <= biggestSize),
            BoardModel.Grid4x4(isUnlocked = !allLevelsLocked && 16 <= biggestSize),
            BoardModel.Grid4x5(isUnlocked = !allLevelsLocked && 20 <= biggestSize),
            BoardModel.Grid4x6(isUnlocked = !allLevelsLocked && 24 <= biggestSize),
            BoardModel.Grid5x6(isUnlocked = !allLevelsLocked && 30 <= biggestSize)
        )

        val getBoards = FakeGetBoardsUseCase(testDispatcher, get()).apply {
            result = Result.success(allLevels)
        }
        val getUnlockedCount = FakeGetUnlockedCardPairsCountUseCase(testDispatcher, get()).apply {
            result = Result.success(unlockedCount)
        }
        val cardRepository = FakeCardRepository(get(), get(), get()).apply {
            allCardPairsOverride = List(totalPairs) { fakeCardPair() }
        }

        return CalculateNextCardPairCostUseCase(
            testDispatcher,
            getUnlockedCount,
            getBoards,
            cardRepository
        )
    }

    private fun fakeCardPair() = CardPairModel(
        first = CardModel.Empty,
        second = CardModel.Empty
    )
}
