package com.wojdor.memolki.ui.feature.chooseboard

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.GetBoardsUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnBoardClick
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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

    private lateinit var sut: ChooseBoardViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ChooseBoardViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            getBoardsUseCase,
            hasPlayedTodayDailyChallengeUseCase
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
}
