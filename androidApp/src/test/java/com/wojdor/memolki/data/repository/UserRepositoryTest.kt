package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
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
class UserRepositoryTest : AppTest() {

    private val userLocalDataSource: UserLocalDataSource by inject()

    private val encryptor: Encryptor by inject()

    private lateinit var sut: UserRepository

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when getCoins then should return decrypted value`() = runTest {
        // given
        val expected = 123L

        userLocalDataSource.setEncryptedCoinsAndTotalCoins { _, _ ->
            encryptor.encrypt(expected) to
                    encryptor.encrypt(expected)
        }

        // when
        val result = sut.getCoins().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when addCoins then should update coins in data source`() = runTest {
        // given
        val initialCoins = 100L
        val addedCoins = 23L
        userLocalDataSource.setEncryptedCoinsAndTotalCoins { _, _ ->
            encryptor.encrypt(initialCoins) to
                    encryptor.encrypt(initialCoins)
        }

        // when
        sut.addCoins(addedCoins)
        val result = sut.getCoins().first()

        // then
        val expected = initialCoins + addedCoins
        assertEquals(expected, result)
    }

    @Test
    fun `when getTotalCoins then should return decrypted value`() = runTest {
        // given
        val totalCoins = 456L
        userLocalDataSource.setEncryptedCoinsAndTotalCoins { _, _ ->
            encryptor.encrypt(totalCoins) to
                    encryptor.encrypt(totalCoins)
        }

        // when
        val result = sut.getTotalCoins().first()

        // then
        assertEquals(totalCoins, result)
    }

    @Test
    fun `when getTotalMatchedCardPairCount then should return decrypted value`() = runTest {
        // given
        val expected = 12L
        userLocalDataSource.setEncryptedTotalCardPairsMatched {
            encryptor.encrypt(expected)
        }

        // when
        val result = sut.getTotalCardPairsMatched().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when incrementTotalMatchedCardPairCount then should increment value in data source`() =
        runTest {
            // given
            val initialCount = 12L
            userLocalDataSource.setEncryptedTotalCardPairsMatched {
                encryptor.encrypt(initialCount)
            }

            // when
            sut.incrementTotalCardPairsMatched()

            // then
            val result = sut.getTotalCardPairsMatched().first()
            val expected = initialCount + 1
            assertEquals(expected, result)
        }

    @Test
    fun `when getTotalGamesPlayed then should return decrypted value`() = runTest {
        // given
        val expected = 34L
        userLocalDataSource.setEncryptedTotalGamesPlayed {
            encryptor.encrypt(expected)
        }

        // when
        val result = sut.getTotalGamesPlayed().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when incrementTotalGamesPlayed then should increment value in data source`() = runTest {
        // given
        val initialCount = 34L
        userLocalDataSource.setEncryptedTotalGamesPlayed {
            encryptor.encrypt(initialCount)
        }

        // when
        sut.incrementTotalGamesPlayed()

        // then
        val result = sut.getTotalGamesPlayed().first()
        val expected = initialCount + 1
        assertEquals(expected, result)
    }

    @Test
    fun `when getUnlockedCardPairsFromAdsCount then should return decrypted value`() = runTest {
        // given
        val expected = 5L
        userLocalDataSource.setEncryptedUnlockedCardPairsFromAdsCount {
            encryptor.encrypt(expected)
        }

        // when
        val result = sut.getUnlockedCardPairsFromAdsCount().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when incrementUnlockedCardPairsFromAdsCount then should increment value in data source`() =
        runTest {
            // given
            val initialCount = 5L
            userLocalDataSource.setEncryptedUnlockedCardPairsFromAdsCount {
                encryptor.encrypt(initialCount)
            }

            // when
            sut.incrementUnlockedCardPairsFromAdsCount()

            // then
            val result = sut.getUnlockedCardPairsFromAdsCount().first()
            val expected = initialCount + 1
            assertEquals(expected, result)
        }

    @Test
    fun `when getLevel with no data then should return default of one`() = runTest {
        // when
        val result = sut.getLevel("2x3").first()

        // then
        assertEquals(1L, result)
    }

    @Test
    fun `when incrementLevel then should return incremented value`() = runTest {
        // when
        val result = sut.incrementLevel("2x3")

        // then
        assertEquals(2L, result)
    }

    @Test
    fun `when incrementLevel twice then should return second incremented value`() =
        runTest {
            // given
            sut.incrementLevel("2x3")

            // when
            val result = sut.incrementLevel("2x3")

            // then
            assertEquals(3L, result)
        }

    @Test
    fun `when getLevel for different levels then should return independent counts`() =
        runTest {
            // given
            sut.incrementLevel("2x3")
            sut.incrementLevel("2x3")
            sut.incrementLevel("4x4")

            // when
            val result2x3 = sut.getLevel("2x3").first()
            val result4x4 = sut.getLevel("4x4").first()

            // then
            assertEquals(3L, result2x3)
            assertEquals(2L, result4x4)
        }

    @Test
    fun `when setLevel then level is stored`() = runTest {
        // when
        sut.setLevel("2x3", 42L)

        // then
        val result = sut.getLevel("2x3").first()
        assertEquals(42L, result)
    }

    @Test
    fun `when removeCoins then coins are reduced`() = runTest {
        // given
        sut.addCoins(100L)

        // when
        sut.removeCoins(30L)

        // then
        val result = sut.getCoins().first()
        assertEquals(70L, result)
    }

    @Test
    fun `when getHasReceivedShareReward with no data then returns false`() = runTest {
        // when
        val result = sut.getHasReceivedShareReward().first()

        // then
        assertEquals(false, result)
    }

    @Test
    fun `when setHasReceivedShareReward then returns true`() = runTest {
        // given
        sut.setHasReceivedShareReward()

        // when
        val result = sut.getHasReceivedShareReward().first()

        // then
        assertEquals(true, result)
    }

    @Test
    fun `when getDailyStreakCount with no data then returns zero`() = runTest {
        // when
        val result = sut.getDailyStreakCount().first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when setDailyStreakData then count and timestamp are stored`() = runTest {
        // when
        sut.setDailyStreakData(5L, 1000L)

        // then
        assertEquals(5L, sut.getDailyStreakCount().first())
        assertEquals(1000L, sut.getLastDailyStreakCollectedTimestamp().first())
    }

    @Test
    fun `when getLastShopAdShownTimestamp with no data then returns zero`() = runTest {
        // when
        val result = sut.getLastShopAdShownTimestamp().first()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when setLastShopAdShownTimestamp then timestamp is stored`() = runTest {
        // when
        sut.setLastShopAdShownTimestamp(5000L)

        // then
        val result = sut.getLastShopAdShownTimestamp().first()
        assertEquals(5000L, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when addCoins with negative value then throws`() = runTest {
        sut.addCoins(-1L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when removeCoins with negative value then throws`() = runTest {
        sut.removeCoins(-1L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when setDailyStreakData with negative count then throws`() = runTest {
        sut.setDailyStreakData(-1L, 1000L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when setDailyStreakData with negative timestamp then throws`() = runTest {
        sut.setDailyStreakData(5L, -1L)
    }

}
