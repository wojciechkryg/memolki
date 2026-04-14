package com.wojdor.memolki.ui.feature.chooseboard

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.GetBoardsUseCase
import com.wojdor.memolki.domain.usecase.HasAnyDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenDailyChallengeScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnBoardClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeHistoryClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnLockedBoardClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChooseBoardViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var getBoardsUseCase: GetBoardsUseCase

    @Inject
    lateinit var hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase

    @Inject
    lateinit var hasAnyDailyChallengeUseCase: HasAnyDailyChallengeUseCase

    private lateinit var sut: ChooseBoardViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ChooseBoardViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            getBoardsUseCase,
            hasPlayedTodayDailyChallengeUseCase,
            hasAnyDailyChallengeUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
            verify { hapticFeedback.vibrateLow() }
        }

    @Test
    fun `when OnDailyChallengeClick then haptic feedback is triggered`() =
        runTest {
            // when
            sut.sendIntent(OnDailyChallengeClick)
            testScheduler.advanceUntilIdle()

            // then
            verify { hapticFeedback.vibrateLow() }
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
            verify { hapticFeedback.vibrateLow() }
        }

    @Test
    fun `when created then daily challenge status is checked`() = runTest {
        // when
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertFalse(state.isDailyChallengeCompleted)
    }
}
