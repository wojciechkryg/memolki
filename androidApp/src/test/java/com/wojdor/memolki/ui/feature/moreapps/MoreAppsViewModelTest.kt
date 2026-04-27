package com.wojdor.memolki.ui.feature.moreapps

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import com.wojdor.memolki.test.verifyOnce
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.OpenApp
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.ShowAppInstall
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsIntent.OnAppClick
import com.wojdor.memolki.test.fake.FakeAnalytics
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class MoreAppsViewModelTest : AppTest() {

    private val analytics: FakeAnalytics by inject()

    private val hapticFeedback: FakeHapticFeedback by inject()

    private val fakeAppInstalledProvider: FakeAppInstalledProvider by inject()

    private lateinit var sut: MoreAppsViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when initial load is done then the state is updated with menu`() = runTest {
        sut.uiState.test {
            // given
            skipItems(1)

            // when
            val state = awaitItem()

            // then
            assertEquals(AppModel.VegetableHalf, state.apps[0])
            assertEquals(AppModel.MammalSide, state.apps[1])
        }
    }

    @Test
    fun `when OnAppClick intent is send then the ShowAppInstall effect is send`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = false

        sut.uiEffect.test {
            // when
            sut.sendIntent(OnAppClick(app))

            // then
            assertEquals(ShowAppInstall(app), awaitItem())
        }
    }

    @Test
    fun `when OnAppClick intent is sent with installed app then cross promotion app opened is logged`() =
        runTest {
            // given
            val app = AppModel.FruitHalf
            fakeAppInstalledProvider.mockAppInstalled = true

            sut.uiEffect.test {
                // when
                sut.sendIntent(OnAppClick(app))
                awaitItem()

                // then
                assertEquals(app.appId, analytics.lastCrossPromotionAppOpened)
            }
        }

    @Test
    fun `when OnAppClick intent is sent with not installed app then cross promotion store opened is logged`() =
        runTest {
            // given
            val app = AppModel.FruitHalf
            fakeAppInstalledProvider.mockAppInstalled = false

            sut.uiEffect.test {
                // when
                sut.sendIntent(OnAppClick(app))
                awaitItem()

                // then
                assertEquals(app.appId, analytics.lastCrossPromotionStoreOpened)
            }
        }

    @Test
    fun `when OnAppClick intent is send then the OpenApp effect is send`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = true

        sut.uiEffect.test {
            // when
            sut.sendIntent(OnAppClick(app))

            // then
            assertEquals(OpenApp(app), awaitItem())
        }
    }

    @Test
    fun `when OnAppClick then haptic feedback is triggered`() = runTest {
        // given
        val app = AppModel.FruitHalf
        fakeAppInstalledProvider.mockAppInstalled = false

        // when
        sut.sendIntent(OnAppClick(app))
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }
}
