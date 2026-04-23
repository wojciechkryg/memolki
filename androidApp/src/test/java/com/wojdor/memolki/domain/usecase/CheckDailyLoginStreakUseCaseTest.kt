package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CheckDailyLoginStreakUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private val timeProvider: TimeProvider by inject()

    private val fakeTimeProvider get() = timeProvider as FakeTimeProvider

    private lateinit var sut: CheckDailyLoginStreakUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CheckDailyLoginStreakUseCase(testDispatcher, userRepository, timeProvider)
    }

    @Test
    fun `when first time then reward is available with day 1`() = runTest {
        sut().test {
            val result = awaitItem().getOrThrow()
            assertTrue(result.isRewardAvailable)
            assertEquals(1, result.streakDay)
            assertEquals(1L, result.coinsReward)
            awaitComplete()
        }
    }

    @Test
    fun `when collected today then reward is not available`() = runTest {
        // given
        userRepository.setDailyStreakData(1L, timeProvider.currentTimeMillis())

        // when
        sut().test {
            val result = awaitItem().getOrThrow()

            // then
            assertFalse(result.isRewardAvailable)
            assertEquals(0L, result.coinsReward)
            awaitComplete()
        }
    }

    @Test
    fun `when consecutive day then streak increments`() = runTest {
        // given
        val yesterday = fakeTimeProvider.mockCurrentDate.minus(1, DateTimeUnit.DAY)
        userRepository.setDailyStreakData(
            2L,
            yesterday.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )

        // when
        sut().test {
            val result = awaitItem().getOrThrow()

            // then
            assertTrue(result.isRewardAvailable)
            assertEquals(3, result.streakDay)
            assertEquals(3L, result.coinsReward)
            awaitComplete()
        }
    }

    @Test
    fun `when gap of two days then streak resets to 1`() = runTest {
        // given
        val twoDaysAgo = fakeTimeProvider.mockCurrentDate.minus(2, DateTimeUnit.DAY)
        userRepository.setDailyStreakData(
            5L,
            twoDaysAgo.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )

        // when
        sut().test {
            val result = awaitItem().getOrThrow()

            // then
            assertTrue(result.isRewardAvailable)
            assertEquals(1, result.streakDay)
            assertEquals(1L, result.coinsReward)
            awaitComplete()
        }
    }

    @Test
    fun `when streak reaches max reward day then coins are capped`() = runTest {
        // given
        val yesterday = fakeTimeProvider.mockCurrentDate.minus(1, DateTimeUnit.DAY)
        userRepository.setDailyStreakData(
            4L,
            yesterday.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )

        // when
        sut().test {
            val result = awaitItem().getOrThrow()

            // then
            assertTrue(result.isRewardAvailable)
            assertEquals(5, result.streakDay)
            assertEquals(CheckDailyLoginStreakUseCase.MAX_DAILY_REWARD, result.coinsReward)
            awaitComplete()
        }
    }
}
