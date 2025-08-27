package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RewardCoinsForLevelUseCaseTest : AppTest() {

    private lateinit var sut: RewardCoinsForLevelUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = RewardCoinsForLevelUseCase(
            testDispatcher,
            UserRepository(
                MockEncryptor(),
                UserLocalDataSource(MockDataStore())
            )
        )
    }

    @Test
    fun `when level with 3 pairs then reward 1 coin`() = runTest {
        // given
        val level = LevelModel.Grid2x3

        // when
        val result = sut(level).first()

        // then
        val expectedCoins = 1L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when level with 6 pairs then reward 3 coins`() = runTest {
        // given
        val level = LevelModel.Grid3x4

        // when
        val result = sut(level).first()

        // then
        val expectedCoins = 3L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when level with 16 pairs then reward 5 coins`() = runTest {
        // given
        val level = LevelModel.Grid4x4

        // when
        val result = sut(level).first()

        // then
        val expectedCoins = 5L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when level with 20 pairs then reward 7 coins`() = runTest {
        // given
        val level = LevelModel.Grid4x5

        // when
        val result = sut(level).first()

        // then
        val expectedCoins = 7L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }

    @Test
    fun `when level with 30 pairs then reward 13 coins`() = runTest {
        // given
        val level = LevelModel.Grid5x6

        // when
        val result = sut(level).first()

        // then
        val expectedCoins = 13L
        val expected = Result.success(expectedCoins)
        assertEquals(expected, result)
    }
}
