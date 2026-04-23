package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeTimeProvider
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.datetime.LocalDate
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HasPlayedTodayDailyChallengeUseCaseTest : AppTest() {

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private lateinit var sut: HasPlayedTodayDailyChallengeUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when played today then return true`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDays()
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
        val today = LocalDate(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDays()
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
        val today = LocalDate(2026, 3, 24)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDays()
        val futureEpochDay = LocalDate(2026, 3, 26).toEpochDays()
        coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns false
        coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns futureEpochDay

        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(true), result)
    }
}
