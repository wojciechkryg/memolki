package com.wojdor.memolki.data.local.user

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserLocalDataSourceTest : AppTest() {

    private lateinit var sut: UserLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = UserLocalDataSource(MockDataStore())
    }

    @Test
    fun `when no coins then return default value`() = runTest {
        // when
        val result = sut.coins.first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when coins exist then return this value`() = runTest {
        // given
        val expected = 123L
        sut.setCoins(expected)

        // when
        val result = sut.coins.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when no total coins then return default value`() = runTest {
        // when
        val result = sut.totalCoins.first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when total coins exist then return this value`() =
        runTest {
            // given
            val expected = 123L
            sut.setTotalCoins(expected)

            // when
            val result = sut.totalCoins.first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `when no total matched card pairs then return default value`() = runTest {
        // when
        val result = sut.totalMatchedCardPairCount.first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when total matched card pairs exist then return this value`() =
        runTest {
            // given
            val expected = 123L
            sut.setTotalMatchedCardPairCount(expected)

            // when
            val result = sut.totalMatchedCardPairCount.first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `when no total games played then return default value`() = runTest {
        // when
        val result = sut.totalGamesPlayed.first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when total games played exist then return this value`() = runTest {
        // given
        val expected = 123L
        sut.setTotalGamesPlayed(expected)

        // when
        val result = sut.totalGamesPlayed.first()

        // then
        assertEquals(expected, result)
    }
}
