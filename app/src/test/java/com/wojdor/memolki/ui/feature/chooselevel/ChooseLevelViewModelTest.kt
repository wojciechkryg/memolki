package com.wojdor.memolki.ui.feature.chooselevel

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelEffect.OpenGameScreen
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelIntent.OnLevelClicked
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChooseLevelViewModelTest : AppTest() {

    private lateinit var sut: ChooseLevelViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ChooseLevelViewModel(
            savedStateHandle = savedStateHandle,
            getLevelsUseCase = GetLevelsUseCase(testDispatcher),
        )
    }

    @Test
    fun `when OnLevelClicked intent is send then the OpenGameScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // given
                val levelModel = LevelModel.Grid2x3

                // when
                sut.sendIntent(OnLevelClicked(levelModel))

                // then
                assertEquals(OpenGameScreen(levelModel), awaitItem())
            }
        }
}
