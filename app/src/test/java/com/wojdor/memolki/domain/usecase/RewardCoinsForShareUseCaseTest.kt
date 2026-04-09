package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RewardCoinsForShareUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: RewardCoinsForShareUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = RewardCoinsForShareUseCase(testDispatcher, userRepository)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when called first time then rewards coins and returns true`() = runTest {
        sut().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
            awaitComplete()
        }
        assertEquals(RewardCoinsForShareUseCase.SHARE_REWARD_COINS, userRepository.getCoins().first())
    }

    @Test
    fun `when called twice then rewards coins only once`() = runTest {
        // given
        sut().first()

        // when
        sut().test {
            val result = awaitItem()

            // then
            assertTrue(result.isSuccess)
            assertFalse(result.getOrThrow())
            awaitComplete()
        }
        assertEquals(RewardCoinsForShareUseCase.SHARE_REWARD_COINS, userRepository.getCoins().first())
    }
}
