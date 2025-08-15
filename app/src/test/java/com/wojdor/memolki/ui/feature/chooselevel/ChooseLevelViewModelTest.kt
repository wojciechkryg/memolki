package com.wojdor.memolki.ui.feature.chooselevel

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelIntent.OnLevelClicked
import com.wojdor.memolki.verifyOnce
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChooseLevelViewModelTest : AppTest() {

    @RelaxedMockK
    private lateinit var getLevelsUseCase: GetLevelsUseCase

    @InjectMockKs
    private lateinit var sut: ChooseLevelViewModel

    @Test
    fun `when view model is created then levels are loaded`() {
        // then
        verifyOnce {
            @Suppress("UnusedFlow")
            getLevelsUseCase()
        }
    }

    @Test
    fun `when OnLevelClicked intent is send then the OpenGameScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // given
                val levelModel = LevelModel.Level2x3

                // when
                sut.sendIntent(OnLevelClicked(levelModel))

                // then
                assertEquals(OpenGameScreen(levelModel), awaitItem())
            }
        }
}
