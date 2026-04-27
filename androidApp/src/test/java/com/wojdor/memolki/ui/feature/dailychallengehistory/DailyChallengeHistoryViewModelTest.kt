package com.wojdor.memolki.ui.feature.dailychallengehistory

import app.cash.turbine.test
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.fake.FakeAnalytics
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class DailyChallengeHistoryViewModelTest : AppTest() {

    private val analytics: FakeAnalytics by inject()

    private val hapticFeedback: FakeHapticFeedback by inject()

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private val dailyChallengeShareFormatter: DailyChallengeShareFormatter by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private lateinit var sut: DailyChallengeHistoryViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 4, 11)
    }

    @Test
    fun `when created then today epoch day is set`() = runTest {
        // when
        sut = get()
        testScheduler.advanceUntilIdle()

        // then
        val expected = LocalDate(2026, 4, 11).toEpochDays()
        assertEquals(expected, sut.uiState.value.todayEpochDay)
    }

    @Test
    fun `when created then history opened analytics is logged`() = runTest {
        // when
        sut = get()
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, analytics.dailyChallengeHistoryOpenedCount)
    }

    @Test
    fun `when challenges exist then state is updated`() = runTest {
        // given
        dailyChallengeDao.insertResult(
            DailyChallengeEntity(
                epochDay = 20001L,
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 45000L,
                cardFlipCounts = "2,2;2,2"
            )
        )

        // when
        sut = get()
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, sut.uiState.value.challenges.size)
        assertEquals(20001L, sut.uiState.value.challenges[0].epochDay)
    }

    @Test
    fun `when OnShareClick then share effect is sent with formatted text`() = runTest {
        // given
        sut = get()
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
        sut = get()
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
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }

    @Test
    fun `when OnShareClick then share analytics is logged`() = runTest {
        // given
        sut = get()
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
        assertEquals(20001L, analytics.lastDailyChallengeHistoryShareClicked)
    }

    @Test
    fun `when loadHistory fails then challenges remain empty`() = runTest {
        // given
        dailyChallengeDao.failureOnGetAll = RuntimeException("DB error")

        // when
        sut = get()
        testScheduler.advanceUntilIdle()

        // then
        assertTrue(sut.uiState.value.challenges.isEmpty())
    }
}
