package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RewardCoinsForShareUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: RewardCoinsForShareUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when called first time then rewards coins and returns true`() = runTest {
        sut().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
            awaitComplete()
        }
        assertEquals(
            RewardCoinsForShareUseCase.SHARE_REWARD_COINS,
            userRepository.getCoins().first()
        )
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
        assertEquals(
            RewardCoinsForShareUseCase.SHARE_REWARD_COINS,
            userRepository.getCoins().first()
        )
    }
}
