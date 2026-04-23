package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CollectDailyStreakRewardUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private val checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase by inject()

    private val timeProvider: TimeProvider by inject()

    private lateinit var sut: CollectDailyStreakRewardUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when first collection then rewards day 1 coins`() = runTest {
        sut().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(1L, result.getOrThrow())
            awaitComplete()
        }
        assertEquals(1L, userRepository.getCoins().first())
    }

    @Test
    fun `when collected then coins match the rewarded amount`() = runTest {
        // given
        val coinsBefore = userRepository.getCoins().first()

        // when
        val rewardedCoins = sut().first().getOrThrow()

        // then
        val coinsAfter = userRepository.getCoins().first()
        assertEquals(coinsBefore + rewardedCoins, coinsAfter)
    }
}
