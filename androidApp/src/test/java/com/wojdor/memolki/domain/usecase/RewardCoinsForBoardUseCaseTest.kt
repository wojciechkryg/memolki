package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RewardCoinsForBoardUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: RewardCoinsForBoardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when board with 3 pairs then reward 1 coin`() = runTest {
        // given
        val board = BoardModel.Grid2x3()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 1L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when board with 6 pairs then reward 3 coins`() = runTest {
        // given
        val board = BoardModel.Grid3x4()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 3L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when board with 16 pairs then reward 5 coins`() = runTest {
        // given
        val board = BoardModel.Grid4x4()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 5L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when board with 20 pairs then reward 7 coins`() = runTest {
        // given
        val board = BoardModel.Grid4x5()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 7L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when board with 24 pairs then reward 10 coins`() = runTest {
        // given
        val board = BoardModel.Grid4x6()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 10L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when board with 30 pairs then reward 13 coins`() = runTest {
        // given
        val board = BoardModel.Grid5x6()

        // when
        val result = sut(board).first()

        // then
        val expectedCoins = 13L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }
}
