package com.wojdor.memolki.ui.feature.chooselevel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelIntent.OnLevelClick
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChooseLevelViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var getLevelsUseCase: GetLevelsUseCase

    private lateinit var sut: ChooseLevelViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ChooseLevelViewModel(
            savedStateHandle,
            hapticFeedback,
            getLevelsUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when OnLevelClick then send OpenGameScreen effect`() =
        runTest {
            sut.uiEffect.test {
                // given
                val levelModel = LevelModel.Grid2x3()

                // when
                sut.sendIntent(OnLevelClick(levelModel))

                // then
                assertEquals(OpenGameScreen(levelModel), awaitItem())
            }
        }
}
