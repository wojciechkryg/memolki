package com.wojdor.memolki.ui.feature.enablenotifications

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EnableNotificationsViewModelTest : AppTest() {

    private lateinit var sut: EnableNotificationsViewModel

    @Before
    override fun setup() {
        super.setup()
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    private fun createSut(destination: String = "game") {
        val savedStateHandle = SavedStateHandle(mapOf("destination" to destination))
        sut = EnableNotificationsViewModel(savedStateHandle)
    }

    @Test
    fun `when OnEnableClick then RequestNotificationPermission effect is sent`() = runTest {
        // given
        createSut()

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnEnableClick)

            // then
            assertEquals(EnableNotificationsEffect.RequestNotificationPermission, awaitItem())
        }
    }

    @Test
    fun `when OnPermissionResult with game destination then NavigateToGame effect is sent`() =
        runTest {
            // given
            createSut("game")

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult)

                // then
                assertEquals(EnableNotificationsEffect.NavigateToGame, awaitItem())
            }
        }

    @Test
    fun `when OnPermissionResult with menu destination then NavigateToMenu effect is sent`() =
        runTest {
            // given
            createSut("menu")

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult)

                // then
                assertEquals(EnableNotificationsEffect.NavigateToMenu, awaitItem())
            }
        }

    @Test
    fun `when OnPermissionResult with collection destination then NavigateToCollection effect is sent`() =
        runTest {
            // given
            createSut("collection")

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult)

                // then
                assertEquals(EnableNotificationsEffect.NavigateToCollection, awaitItem())
            }
        }

    @Test
    fun `when OnLaterClick with game destination then NavigateToGame effect is sent`() = runTest {
        // given
        createSut("game")

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnLaterClick)

            // then
            assertEquals(EnableNotificationsEffect.NavigateToGame, awaitItem())
        }
    }
}
