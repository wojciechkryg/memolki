package com.wojdor.memolki.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UserLocalDataSourceTest : AppTest() {

    private val dataStore: DataStore<Preferences> by inject()

    private lateinit var sut: UserLocalDataSource

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
        val expectedCoins = "3"
        val expectedTotalCoins = "123"
        sut.setEncryptedCoinsAndTotalCoins { _, _ -> expectedCoins to expectedTotalCoins }

        // when
        val resultCoins = sut.encryptedCoins.first()
        val resultTotalCoins = sut.encryptedTotalCoins.first()

        // then
        assertEquals(expectedCoins, resultCoins)
        assertEquals(expectedTotalCoins, resultTotalCoins)
    }

    @Test
    fun `when no total coins then return default value`() = runTest {
        // when
        val result = sut.encryptedTotalCoins.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when no total matched card pairs then return default value`() = runTest {
        // when
        val result = sut.encryptedTotalCardPairsMatched.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when total matched card pairs exist then return this value`() =
        runTest {
            // given
            val expected = "123"
            sut.setEncryptedTotalCardPairsMatched { expected }

            // when
            val result = sut.encryptedTotalCardPairsMatched.first()

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
        val expected = "123"
        sut.setEncryptedTotalGamesPlayed { expected }

        // when
        val result = sut.encryptedTotalGamesPlayed.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when no level then return default value`() = runTest {
        // when
        val result = sut.encryptedLevel("2x3").first()

        // then
        assertNull(result)
    }

    @Test
    fun `when level exists then return this value`() = runTest {
        // given
        val expected = "5"
        sut.setEncryptedLevel("2x3") { expected }

        // when
        val result = sut.encryptedLevel("2x3").first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when level for different boards then return independent values`() = runTest {
        // given
        sut.setEncryptedLevel("2x3") { "10" }
        sut.setEncryptedLevel("4x4") { "20" }

        // when
        val result2x3 = sut.encryptedLevel("2x3").first()
        val result4x4 = sut.encryptedLevel("4x4").first()

        // then
        assertEquals("10", result2x3)
        assertEquals("20", result4x4)
    }
}
