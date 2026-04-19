package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetBoardsUseCaseTest : AppTest() {

    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private lateinit var sut: GetBoardsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetBoardsUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase
        )
    }

    @Test
    fun `when called then returns list of levels`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    BoardModel.Grid2x3(isUnlocked = true),
                    BoardModel.Grid3x4(isUnlocked = false),
                    BoardModel.Grid4x4(isUnlocked = false),
                    BoardModel.Grid4x5(isUnlocked = false),
                    BoardModel.Grid4x6(isUnlocked = false),
                    BoardModel.Grid5x6(isUnlocked = false),
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when many cards unlocked then larger boards are also unlocked`() = runTest {
        // given
        listOf("watermelon", "mango", "peach", "pineapple", "blueberry").forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it)
        }

        // when
        sut().test {
            // then
            val result = awaitItem().getOrThrow()
            assertTrue(result[0].isUnlocked)
            assertTrue(result[1].isUnlocked)
            assertTrue(result[2].isUnlocked)
            assertTrue(result[3].isUnlocked)
            assertEquals(6, result.size)
            awaitComplete()
        }
    }

    @Test
    fun `when all available cards unlocked then boards up to 4x5 are unlocked`() = runTest {
        // given
        listOf("watermelon", "mango", "peach", "pineapple", "blueberry").forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it)
        }

        // when
        sut().test {
            // then
            val result = awaitItem().getOrThrow()
            assertTrue(result[0].isUnlocked)  // 2x3 needs 3
            assertTrue(result[1].isUnlocked)  // 3x4 needs 6
            assertTrue(result[2].isUnlocked)  // 4x4 needs 8
            assertTrue(result[3].isUnlocked)  // 4x5 needs 10
            assertFalse(result[4].isUnlocked) // 4x6 needs 12
            assertFalse(result[5].isUnlocked) // 5x6 needs 15
            awaitComplete()
        }
    }
}
