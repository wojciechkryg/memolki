package com.wojdor.memolki.ui.feature.enablenotifications

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.util.analytics.Analytics
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class EnableNotificationsViewModelTest : AppTest() {

    private val analytics: Analytics by inject()
    private val savedStateHandle: SavedStateHandle by inject()

    private lateinit var sut: EnableNotificationsViewModel

    @Before
    override fun setup() {
        super.setup()
    }

    private fun createSut(destination: String = "game") {
        savedStateHandle["destination"] = destination
        sut = get()
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
            createSut(EnableNotificationDestination.GAME.route)

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))

                // then
                assertEquals(EnableNotificationsEffect.NavigateToGame(""), awaitItem())
            }
        }

    @Test
    fun `when OnPermissionResult with menu destination then NavigateToMenu effect is sent`() =
        runTest {
            // given
            createSut(EnableNotificationDestination.MENU.route)

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))

                // then
                assertEquals(EnableNotificationsEffect.NavigateToMenu, awaitItem())
            }
        }

    @Test
    fun `when OnPermissionResult with collection destination then NavigateToCollection effect is sent`() =
        runTest {
            // given
            createSut(EnableNotificationDestination.COLLECTION.route)

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))

                // then
                assertEquals(EnableNotificationsEffect.NavigateToCollection, awaitItem())
            }
        }

    @Test
    fun `when OnLaterClick with game destination then NavigateToGame effect is sent`() = runTest {
        // given
        createSut(EnableNotificationDestination.GAME.route)

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnLaterClick)

            // then
            assertEquals(EnableNotificationsEffect.NavigateToGame(""), awaitItem())
        }
    }

    @Test
    fun `when notification permission is granted then logNotificationEnabled is called with true`() =
        runTest {
            // given
            createSut()

            // when
            sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logNotificationEnabled(true) }
        }

    @Test
    fun `when notification permission is denied then logNotificationEnabled is called with false`() =
        runTest {
            // given
            createSut()

            // when
            sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(false))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logNotificationEnabled(false) }
        }

    @Test
    fun `when later is clicked then logNotificationEnabled is called with false`() = runTest {
        // given
        createSut()

        // when
        sut.sendIntent(EnableNotificationsIntent.OnLaterClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logNotificationEnabled(false) }
    }

    @Test
    fun `when OnPermissionResult with shop destination then NavigateToShop effect is sent`() =
        runTest {
            // given
            createSut(EnableNotificationDestination.SHOP.route)

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))

                // then
                assertEquals(EnableNotificationsEffect.NavigateToShop, awaitItem())
            }
        }

    @Test
    fun `when OnLaterClick with menu destination then NavigateToMenu effect is sent`() = runTest {
        // given
        createSut(EnableNotificationDestination.MENU.route)

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnLaterClick)

            // then
            assertEquals(EnableNotificationsEffect.NavigateToMenu, awaitItem())
        }
    }

    @Test
    fun `when OnLaterClick with collection destination then NavigateToCollection effect is sent`() =
        runTest {
            // given
            createSut(EnableNotificationDestination.COLLECTION.route)

            sut.uiEffect.test {
                // when
                sut.sendIntent(EnableNotificationsIntent.OnLaterClick)

                // then
                assertEquals(EnableNotificationsEffect.NavigateToCollection, awaitItem())
            }
        }

    @Test
    fun `when OnLaterClick with shop destination then NavigateToShop effect is sent`() = runTest {
        // given
        createSut(EnableNotificationDestination.SHOP.route)

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnLaterClick)

            // then
            assertEquals(EnableNotificationsEffect.NavigateToShop, awaitItem())
        }
    }

    @Test
    fun `when unknown destination then defaults to menu`() = runTest {
        // given
        createSut("unknown_destination")

        sut.uiEffect.test {
            // when
            sut.sendIntent(EnableNotificationsIntent.OnPermissionResult(true))

            // then
            assertEquals(EnableNotificationsEffect.NavigateToMenu, awaitItem())
        }
    }
}
