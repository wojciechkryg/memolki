package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IncrementTotalGamesPlayedUseCaseTest : AppTest() {

    private lateinit var userRepository: UserRepository
    private lateinit var sut: IncrementTotalGamesPlayedUseCase

    @Before
    override fun setup() {
        super.setup()
        userRepository = UserRepository(
            MockEncryptor(),
            UserLocalDataSource(MockDataStore())
        )
        sut = IncrementTotalGamesPlayedUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when execute is successful then returns success`() = runTest {
        // when
        sut().test {
            // then
            assertEquals(Result.success(Unit), awaitItem())
            awaitComplete()
        }
        userRepository.getTotalGamesPlayed().test {
            assertEquals(1L, awaitItem())
        }
    }
}
