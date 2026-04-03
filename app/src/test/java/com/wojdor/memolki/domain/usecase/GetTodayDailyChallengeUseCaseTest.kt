package com.wojdor.memolki.domain.usecase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetTodayDailyChallengeUseCaseTest : AppTest() {

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    @Inject
    lateinit var fakeTimeProvider: FakeTimeProvider

    private lateinit var sut: GetTodayDailyChallengeUseCase

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        sut = GetTodayDailyChallengeUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao),
            fakeTimeProvider
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when entry exists for today then return model`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDay()
        val entity = DailyChallengeEntity(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = "1,2;3,4"
        )
        coEvery { dailyChallengeDao.getResult(epochDay) } returns entity

        // when
        val result = sut().first()

        // then
        val expected = DailyChallengeModel(
            epochDay = epochDay,
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 4))
        )
        assertEquals(Result.success(expected), result)
    }

    @Test
    fun `when no entry exists for today then return failure`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val epochDay = today.toEpochDay()
        coEvery { dailyChallengeDao.getResult(epochDay) } returns null

        // when
        val result = sut().first()

        // then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }
}
