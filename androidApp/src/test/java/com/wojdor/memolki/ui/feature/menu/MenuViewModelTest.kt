package com.wojdor.memolki.ui.feature.menu

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseBoardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenLeaderboardScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenMoreAppsScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnDailyRewardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnLeaderboardClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnMoreAppsClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnPlayClick
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClick
import com.wojdor.memolki.test.fake.FakeAnalytics
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class MenuViewModelTest : AppTest() {

    private val hapticFeedback: FakeHapticFeedback by inject()
    private val analytics: FakeAnalytics by inject()
    private val userRepository: UserRepository by inject()

    private lateinit var sut: MenuViewModel

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
            assertEquals(4, state.menu.size)
            assertEquals(MenuModel.Play, state.menu[0])
            assertEquals(MenuModel.Collection, state.menu[1])
            assertEquals(MenuModel.Leaderboard, state.menu[2])
            assertEquals(MenuModel.Settings, state.menu[3])
        }
    }

    @Test
    fun `when OnPlayClick intent is send then the OpenChooseBoardScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnPlayClick)

                // then
                assertEquals(OpenChooseBoardScreen, awaitItem())
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

    @Test
    fun `when leaderboard is clicked then logLeaderboardOpened is called`() = runTest {
        // when
        sut.sendIntent(OnLeaderboardClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, analytics.leaderboardOpenedCount)
    }

    @Test
    fun `when more apps is clicked then logMoreAppsClicked is called`() = runTest {
        // when
        sut.sendIntent(OnMoreAppsClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, analytics.moreAppsClickedCount)
    }

    @Test
    fun `when OnDailyRewardClick intent is sent then the OpenShopScreen effect is sent`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(OnDailyRewardClick)

                // then
                assertEquals(MenuEffect.OpenShopScreen, awaitItem())
            }
        }

    @Test
    fun `when daily reward is clicked then logShopOpenedFromDailyReward is called`() = runTest {
        // when
        sut.sendIntent(OnDailyRewardClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, analytics.shopOpenedFromDailyRewardCount)
    }

    @Test
    fun `when OnPlayClick then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(OnPlayClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }

    @Test
    fun `when OnCollectionClick then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(OnCollectionClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }

    @Test
    fun `when OnSettingsClick then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(OnSettingsClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }

    @Test
    fun `when leaderboard is clicked then leaderboard scores are sent`() = runTest {
        sut.uiEffect.test {
            // when
            sut.sendIntent(OnLeaderboardClick)
            testScheduler.advanceUntilIdle()

            // then
            skipItems(1)
            assertTrue(awaitItem() is MenuEffect.SendTotalCoinsScore)
            assertTrue(awaitItem() is MenuEffect.SendTotalCardPairsMatchedScore)
        }
    }

    @Test
    fun `when OnScreenResume then daily reward state is refreshed`() = runTest {
        // given
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(MenuIntent.OnScreenResume)
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertTrue(state.menu.isNotEmpty())
    }

    @Test
    fun `when enough games played then other app model is shown`() = runTest {
        // given
        repeat(3) { userRepository.incrementTotalGamesPlayed() }
        testScheduler.advanceUntilIdle()

        sut = get()
        testScheduler.advanceUntilIdle()

        // then
        assertNotNull(sut.uiState.value.otherAppModel)
    }
}
