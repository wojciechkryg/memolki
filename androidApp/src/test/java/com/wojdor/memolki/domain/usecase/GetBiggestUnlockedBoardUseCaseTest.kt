package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
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
class GetBiggestUnlockedBoardUseCaseTest : AppTest() {

    @Inject
    lateinit var getBoardsUseCase: GetBoardsUseCase

    @Inject
    lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource

    private lateinit var sut: GetBiggestUnlockedBoardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetBiggestUnlockedBoardUseCase(testDispatcher, getBoardsUseCase)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
