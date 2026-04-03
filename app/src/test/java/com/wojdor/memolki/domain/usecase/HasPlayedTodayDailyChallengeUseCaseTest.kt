package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeTimeProvider
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HasPlayedTodayDailyChallengeUseCaseTest : AppTest() {

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    @Inject
    lateinit var fakeTimeProvider: FakeTimeProvider

    private lateinit var sut: HasPlayedTodayDailyChallengeUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = HasPlayedTodayDailyChallengeUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao),
            fakeTimeProvider
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when played today then return true`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDay()
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns true
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns epochDay

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }

    @Test
    fun `when not played today then return false`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDay()
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns false
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns null

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(false), result)
    }

    @Test
    fun `when date rolled back then return true`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 24)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDay()
        val futureEpochDay = LocalDate.of(2026, 3, 26).toEpochDay()
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns false
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns futureEpochDay

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }
}
