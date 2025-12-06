package com.wojdor.memolki.ui.feature.menu

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenLeaderboardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenMoreAppsScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnLeaderboardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnMoreAppsClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClick
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenuViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var googlePlayGames: GooglePlayGames

    @Inject
    lateinit var getMenuUseCase: GetMenuUseCase

    @Inject
    lateinit var getMoreAppsUseCase: GetMoreAppsUseCase

    @Inject
    lateinit var getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase

    private lateinit var sut: MenuViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = MenuViewModel(
            savedStateHandle,
            hapticFeedback,
            googlePlayGames,
            getMenuUseCase,
            getMoreAppsUseCase,
            getTotalGamesPlayedUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when initial load is done then the state is updated with menu`() = runTest {
        sut.uiState.test {
            // given
            skipItems(1)

            // when
            val state = awaitItem()

            // then
            assertEquals(3, state.menu.size)
            assertEquals(MenuModel.NewGame, state.menu[0])
            assertEquals(MenuModel.Collection, state.menu[1])
            assertEquals(MenuModel.Settings, state.menu[2])
        }
    }

    @Test
    fun `when OnNewGameClick intent is send then the OpenChooseLevelScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnNewGameClick)

                // then
                assertEquals(OpenChooseLevelScreen, awaitItem())
            }
        }

    @Test
    fun `when OnCollectionClick intent is send then the OpenCollectionScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnCollectionClick)

                // then
                assertEquals(OpenCollectionScreen, awaitItem())
            }
        }

    @Test
    fun `when OnLeaderboardClick intent is send then the OpenLeaderboardScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnLeaderboardClick)

                // then
                assertTrue(awaitItem() is OpenLeaderboardScreen)
            }
        }

    @Test
    fun `when OnSettingsClick intent is send then the OpenSettingsScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnSettingsClick)

                // then
                assertEquals(OpenSettingsScreen, awaitItem())
            }
        }

    @Test
    fun `when OnMoreAppsClick intent is send then the OpenMoreAppsScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnMoreAppsClick)

                // then
                assertEquals(OpenMoreAppsScreen, awaitItem())
            }
        }
}
