package com.wojdor.memolki.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserLocalDataSourceTest : AppTest() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private lateinit var sut: UserLocalDataSource

    @Before
    override fun setup() {
        super.setup()
        sut = UserLocalDataSource(dataStore)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
}
