package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
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
class IncrementUnlockedCardPairsFromAdsCountUseCaseTest : AppTest() {

    private lateinit var userRepository: UserRepository
    private lateinit var sut: IncrementUnlockedCardPairsFromAdsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        userRepository = UserRepository(
            MockEncryptor(),
            UserLocalDataSource(MockDataStore())
        )
        sut = IncrementUnlockedCardPairsFromAdsCountUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when use case is executed then should increment unlocked card pairs from ads count`() =
        runTest {
            // when
            sut().first()

            // then
            assertEquals(1, userRepository.getUnlockedCardPairsFromAdsCount().first())
        }
}
