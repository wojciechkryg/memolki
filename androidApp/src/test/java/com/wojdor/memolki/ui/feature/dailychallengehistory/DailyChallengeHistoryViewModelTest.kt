package com.wojdor.memolki.ui.feature.dailychallengehistory

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.GetAllDailyChallengesUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.media.HapticFeedback
import io.mockk.coEvery
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.koin.test.inject

@ExperimentalCoroutinesApi
class DailyChallengeHistoryViewModelTest : AppTest() {

    private val savedStateHandle: SavedStateHandle by inject()

    private val analytics: Analytics by inject()

    private val hapticFeedback: HapticFeedback by inject()

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private val dailyChallengeShareFormatter: DailyChallengeShareFormatter by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private lateinit var sut: DailyChallengeHistoryViewModel

    @Before
    override fun setup() {
        super.setup()
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 4, 11)
        coEvery { dailyChallengeDao.getAll() } returns emptyList()
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
    fun `when OnShareClick then share effect is sent with formatted text`() = runTest {
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
        val expectedText = dailyChallengeShareFormatter.format(challenge)

        // when
        sut.uiEffect.test {
            sut.sendIntent(DailyChallengeHistoryIntent.OnShareClick(challenge))

            // then
            val effect = awaitItem() as DailyChallengeHistoryEffect.ShareDailyChallenge
            assertEquals(expectedText, effect.text)
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

    @Test
    fun `when loadHistory fails then challenges remain empty`() = runTest {
        // given
        coEvery { dailyChallengeDao.getAll() } throws RuntimeException("DB error")

        // when
        sut = createSut()
        testScheduler.advanceUntilIdle()

        // then
        assertTrue(sut.uiState.value.challenges.isEmpty())
    }
}
