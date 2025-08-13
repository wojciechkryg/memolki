package com.wojdor.memolki.ui.feature.menu

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClicked
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest : AppTest() {

    private var viewModel = MenuViewModel()

    @Test
    fun `when OnNewGameClicked intent is send then the OpenChooseLevelScreen effect is send`() =
        runTest {
            viewModel.uiEffect.test {
                viewModel.sendIntent(OnNewGameClicked)

                assertEquals(OpenChooseLevelScreen, awaitItem())
            }
        }

    @Test
    fun `when OnCollectionClicked intent is send then the OpenCollectionScreen effect is send`() =
        runTest {
            viewModel.uiEffect.test {
                viewModel.sendIntent(OnCollectionClicked)

                assertEquals(OpenCollectionScreen, awaitItem())
            }
        }

    @Test
    fun `when OnSettingsClicked intent is send then the OpenSettingsScreen effect is send`() =
        runTest {
            viewModel.uiEffect.test {
                viewModel.sendIntent(OnSettingsClicked)

                assertEquals(OpenSettingsScreen, awaitItem())
            }
        }
}
