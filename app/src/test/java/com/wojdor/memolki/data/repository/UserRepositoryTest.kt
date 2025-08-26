package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.user.UserLocalDataSource
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
class UserRepositoryTest : AppTest() {

    private lateinit var sut: UserRepository
    private lateinit var userLocalDataSource: UserLocalDataSource
    private val mockEncryptor = MockEncryptor()

    @Before
    override fun setup() {
        super.setup()
        userLocalDataSource = UserLocalDataSource(MockDataStore())
        sut = UserRepository(
            mockEncryptor,
            userLocalDataSource
        )
    }

    @Test
    fun `when getCoins then should return decrypted value`() = runTest {
        // given
        val expected = 123L
        userLocalDataSource.setEncryptedCoins(mockEncryptor.encrypt(expected))

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
        userLocalDataSource.setEncryptedCoins(mockEncryptor.encrypt(initialCoins))
        userLocalDataSource.setEncryptedTotalCoins(mockEncryptor.encrypt(initialCoins))


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
        userLocalDataSource.setEncryptedTotalCoins(MockEncryptor().encrypt(totalCoins))

        // when
        val result = sut.getTotalCoins().first()

        // then
        assertEquals(totalCoins, result)
    }

    @Test
    fun `when getTotalMatchedCardPairCount then should return decrypted value`() = runTest {
        // given
        val expected = 12L
        userLocalDataSource.setEncryptedTotalMatchedCardPairCount(MockEncryptor().encrypt(expected))

        // when
        val result = sut.getTotalMatchedCardPairCount().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when incrementTotalMatchedCardPairCount then should increment value in data source`() =
        runTest {
            // given
            val initialCount = 12L
            userLocalDataSource.setEncryptedTotalMatchedCardPairCount(
                MockEncryptor().encrypt(
                    initialCount
                )
            )

            // when
            sut.incrementTotalMatchedCardPairCount()

            // then
            val result = sut.getTotalMatchedCardPairCount().first()
            val expected = initialCount + 1
            assertEquals(expected, result)
        }

    @Test
    fun `when getTotalGamesPlayed then should return decrypted value`() = runTest {
        // given
        val expected = 34L
        userLocalDataSource.setEncryptedTotalGamesPlayed(MockEncryptor().encrypt(expected))

        // when
        val result = sut.getTotalGamesPlayed().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when incrementTotalGamesPlayed then should increment value in data source`() = runTest {
        // given
        val initialCount = 34L
        userLocalDataSource.setEncryptedTotalGamesPlayed(MockEncryptor().encrypt(initialCount))

        // when
        sut.incrementTotalGamesPlayed()

        // then
        val result = sut.getTotalGamesPlayed().first()
        val expected = initialCount + 1
        assertEquals(expected, result)
    }
}
