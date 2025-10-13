package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserRepositoryTest : AppTest() {

    @Inject
    lateinit var userLocalDataSource: UserLocalDataSource

    @Inject
    lateinit var encryptor: Encryptor

    private lateinit var sut: UserRepository

    @Before
    override fun setup() {
        super.setup()
        sut = UserRepository(
            encryptor,
            userLocalDataSource
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
}
