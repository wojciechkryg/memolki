package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class IncrementTotalGamesPlayedUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: IncrementTotalGamesPlayedUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = IncrementTotalGamesPlayedUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
