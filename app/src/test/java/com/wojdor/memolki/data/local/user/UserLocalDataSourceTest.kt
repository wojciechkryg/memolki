package com.wojdor.memolki.data.local.user

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
        val result = sut.encryptedCoins.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when coins exist then return this value`() = runTest {
        // given
        val expected = "encrypted"
        sut.setEncryptedCoins(expected)

        // when
        val result = sut.encryptedCoins.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when no total coins then return default value`() = runTest {
        // when
        val result = sut.encryptedTotalCoins.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when total coins exist then return this value`() =
        runTest {
            // given
            val expected = "encrypted"
            sut.setEncryptedTotalCoins(expected)

            // when
            val result = sut.encryptedTotalCoins.first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `when no total matched card pairs then return default value`() = runTest {
        // when
        val result = sut.encryptedTotalMatchedCardPairCount.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when total matched card pairs exist then return this value`() =
        runTest {
            // given
            val expected = "encrypted"
            sut.setEncryptedTotalMatchedCardPairCount(expected)

            // when
            val result = sut.encryptedTotalMatchedCardPairCount.first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `when no total games played then return default value`() = runTest {
        // when
        val result = sut.encryptedTotalGamesPlayed.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when total games played exist then return this value`() = runTest {
        // given
        val expected = "encrypted"
        sut.setEncryptedTotalGamesPlayed(expected)

        // when
        val result = sut.encryptedTotalGamesPlayed.first()

        // then
        assertEquals(expected, result)
    }
}
