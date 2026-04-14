package com.wojdor.memolki.ui.feature.dailychallengehistory

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.GetAllDailyChallengesUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.media.HapticFeedback
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DailyChallengeHistoryViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    @Inject
    lateinit var dailyChallengeShareFormatter: DailyChallengeShareFormatter

    @Inject
    lateinit var fakeTimeProvider: FakeTimeProvider

    private lateinit var sut: DailyChallengeHistoryViewModel

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 4, 11)
        coEvery { dailyChallengeDao.getAll() } returns emptyList()
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    private fun createSut(): DailyChallengeHistoryViewModel {
        val getAllDailyChallengesUseCase = GetAllDailyChallengesUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao)
        )
        return DailyChallengeHistoryViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            getAllDailyChallengesUseCase,
            dailyChallengeShareFormatter,
            fakeTimeProvider
        )
    }

    @Test
    fun `when created then today epoch day is set`() = runTest {
        // when
        sut = createSut()
        testScheduler.advanceUntilIdle()

        // then
        val expected = LocalDate.of(2026, 4, 11).toEpochDay()
        assertEquals(expected, sut.uiState.value.todayEpochDay)
    }

    @Test
    fun `when created then history opened analytics is logged`() = runTest {
        // when
        sut = createSut()
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logDailyChallengeHistoryOpened() }
    }

    @Test
    fun `when challenges exist then state is updated`() = runTest {
        // given
        val entities = listOf(
            DailyChallengeEntity(
                epochDay = 20001L,
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 45000L,
                cardFlipCounts = "2,2;2,2"
            )
        )
        coEvery { dailyChallengeDao.getAll() } returns entities

        // when
        sut = createSut()
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, sut.uiState.value.challenges.size)
        assertEquals(20001L, sut.uiState.value.challenges[0].epochDay)
    }

    @Test
    fun `when OnShareClick then share effect is sent`() = runTest {
        // given
        sut = createSut()
        testScheduler.advanceUntilIdle()
        val challenge = DailyChallengeModel(
            epochDay = 20001L,
            mistakeCount = 0,
            starCount = 3,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(2, 2), listOf(2, 2))
        )

        // when
        sut.uiEffect.test {
            sut.sendIntent(DailyChallengeHistoryIntent.OnShareClick(challenge))

            // then
            val effect = awaitItem()
            assertTrue(effect is DailyChallengeHistoryEffect.ShareDailyChallenge)
        }
    }

    @Test
    fun `when OnShareClick then haptic feedback is triggered`() = runTest {
        // given
        sut = createSut()
        testScheduler.advanceUntilIdle()
        val challenge = DailyChallengeModel(
            epochDay = 20001L,
            mistakeCount = 0,
            starCount = 3,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(2, 2))
        )

        // when
        sut.sendIntent(DailyChallengeHistoryIntent.OnShareClick(challenge))
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnShareClick then share analytics is logged`() = runTest {
        // given
        sut = createSut()
        testScheduler.advanceUntilIdle()
        val challenge = DailyChallengeModel(
            epochDay = 20001L,
            mistakeCount = 0,
            starCount = 3,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(2, 2))
        )

        // when
        sut.sendIntent(DailyChallengeHistoryIntent.OnShareClick(challenge))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logDailyChallengeHistoryShareClicked(20001L) }
    }

    private fun assertTrue(condition: Boolean) {
        org.junit.Assert.assertTrue(condition)
    }
}
