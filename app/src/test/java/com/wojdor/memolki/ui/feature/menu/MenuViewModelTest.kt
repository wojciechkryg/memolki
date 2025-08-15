package com.wojdor.memolki.ui.feature.menu

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClicked
import com.wojdor.memolki.verifyOnce
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest : AppTest() {

    @RelaxedMockK
    private lateinit var getMenuUseCase: GetMenuUseCase

    @InjectMockKs
    private lateinit var sut: MenuViewModel

    @Test
    fun `when view model is created then menu is loaded`() {
        // then
        verifyOnce {
            @Suppress("UnusedFlow")
            getMenuUseCase()
        }
    }

    @Test
    fun `when OnNewGameClicked intent is send then the OpenChooseLevelScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnNewGameClicked)

                // then
                assertEquals(OpenChooseLevelScreen, awaitItem())
            }
        }

    @Test
    fun `when OnCollectionClicked intent is send then the OpenCollectionScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnCollectionClicked)

                // then
                assertEquals(OpenCollectionScreen, awaitItem())
            }
        }

    @Test
    fun `when OnSettingsClicked intent is send then the OpenSettingsScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnSettingsClicked)

                // then
                assertEquals(OpenSettingsScreen, awaitItem())
            }
        }
}
