package com.wojdor.memolki.ui.feature.chooseboard

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenDailyChallengeScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnBoardClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeHistoryClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnLockedBoardClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ChooseBoardViewModelTest : AppTest() {

    private val analytics: Analytics by inject()

    private val hapticFeedback: FakeHapticFeedback by inject()

    private lateinit var sut: ChooseBoardViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when OnBoardClick then send OpenGameScreen effect`() =
        runTest {
            sut.uiEffect.test {
                // given
                val boardModel = BoardModel.Grid2x3()

                // when
                sut.sendIntent(OnBoardClick(boardModel))

                // then
                assertEquals(OpenGameScreen(boardModel), awaitItem())
            }
        }

    @Test
    fun `when OnDailyChallengeClick then send OpenDailyChallengeScreen effect`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnDailyChallengeClick)

                // then
                assertEquals(OpenDailyChallengeScreen, awaitItem())
            }
        }

    @Test
    fun `when OnLockedBoardClick then send OpenCollectionScreen effect`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnLockedBoardClick)

                // then
                assertEquals(OpenCollectionScreen, awaitItem())
            }
        }

    @Test
    fun `when OnLockedBoardClick then logCollectionOpenedFromLockedBoard is called`() =
        runTest {
            // when
            sut.sendIntent(OnLockedBoardClick)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logCollectionOpenedFromLockedBoard() }
        }

    @Test
    fun `when OnBoardClick then haptic feedback is triggered`() =
        runTest {
            // when
            sut.sendIntent(OnBoardClick(BoardModel.Grid2x3()))
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(1, hapticFeedback.vibrateLowCount)
        }

    @Test
    fun `when OnDailyChallengeClick then haptic feedback is triggered`() =
        runTest {
            // when
            sut.sendIntent(OnDailyChallengeClick)
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(1, hapticFeedback.vibrateLowCount)
        }

    @Test
    fun `when created then boards are loaded`() = runTest {
        sut.uiState.test {
            skipItems(1)

            // then
            val state = awaitItem()
            assertTrue(state.boards.isNotEmpty())
        }
    }

    @Test
    fun `when OnDailyChallengeHistoryClick then send OpenDailyChallengeHistoryScreen effect`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnDailyChallengeHistoryClick)

                // then
                assertEquals(ChooseBoardEffect.OpenDailyChallengeHistoryScreen, awaitItem())
            }
        }

    @Test
    fun `when OnDailyChallengeHistoryClick then haptic feedback is triggered`() =
        runTest {
            // when
            sut.sendIntent(OnDailyChallengeHistoryClick)
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(1, hapticFeedback.vibrateLowCount)
        }

    @Test
    fun `when created then daily challenge status is checked`() = runTest {
        // when
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertFalse(state.isDailyChallengeCompleted)
    }

    @Test
    fun `when created then daily challenge history is checked`() = runTest {
        // when
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertFalse(state.hasDailyChallengeHistory)
    }
}
