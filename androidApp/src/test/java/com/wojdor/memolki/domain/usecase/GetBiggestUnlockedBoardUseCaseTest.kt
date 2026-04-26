package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetBiggestUnlockedBoardUseCaseTest : AppTest() {

    private val getBoardsUseCase: GetBoardsUseCase by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private lateinit var sut: GetBiggestUnlockedBoardUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when requested level is unlocked then returns requested level`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")

        // when
        sut("3x4").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("3x4", result.getOrThrow().id)
            assertTrue(result.getOrThrow().isUnlocked)
            awaitComplete()
        }
    }

    @Test
    fun `when requested level is locked then returns biggest unlocked level`() = runTest {
        // when
        sut("5x6").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("2x3", result.getOrThrow().id)
            awaitComplete()
        }
    }

    @Test
    fun `when unknown level requested then returns biggest unlocked level`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("peach")

        // when
        sut("auto").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("4x4", result.getOrThrow().id)
            awaitComplete()
        }
    }

    @Test
    fun `when no boards are unlocked then returns first board`() = runTest {
        // given
        val allLocked = listOf(
            BoardModel.Grid2x3(isUnlocked = false),
            BoardModel.Grid3x4(isUnlocked = false),
            BoardModel.Grid4x4(isUnlocked = false)
        )
        val mockGetBoards = mockk<GetBoardsUseCase>()
        every { mockGetBoards() } returns flowOf(Result.success(allLocked))
        val sutWithMock = GetBiggestUnlockedBoardUseCase(testDispatcher, mockGetBoards)

        // when
        sutWithMock("auto").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("2x3", result.getOrThrow().id)
            awaitComplete()
        }
    }
}
