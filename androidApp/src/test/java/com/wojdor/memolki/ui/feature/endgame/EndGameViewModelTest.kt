package com.wojdor.memolki.ui.feature.endgame

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationDestination
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import com.wojdor.memolki.test.fake.FakeLevelCompletePlayer
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class EndGameViewModelTest : AppTest() {

    private val levelCompletePlayer: FakeLevelCompletePlayer by inject()

    private val hapticFeedback: FakeHapticFeedback by inject()

    private val coinsPlayer: CoinsPlayer by inject()

    private val allRewardedAds: AllRewardedAds by inject()

    private val userRepository: UserRepository by inject()

    private val analytics: Analytics by inject()

    private lateinit var sut: EndGameViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when casual end game show is sent then coins are rewarded exactly once`() =
        runTest {
            // given
            val boardModel = BoardModel.Grid2x3(isUnlocked = true)
            val expectedReward = 1L
            val coinsBefore = userRepository.getCoins().first()

            // when
            sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
            testScheduler.advanceUntilIdle()

            // then
            val coinsAfter = userRepository.getCoins().first()
            assertEquals(coinsBefore + expectedReward, coinsAfter)
        }

    @Test
    fun `when OnEndGameShow intent is sent then the state is updated with the board and rewarded coins`() =
        runTest {
            // given
            val boardModel = BoardModel.Grid2x3(isUnlocked = true)
            val rewardedCoins = 1L

            // when
            sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
            testScheduler.advanceUntilIdle()

            // then
            val expected = EndGameState(
                board = boardModel,
                rewardedCoins = rewardedCoins,
                currentCoins = 0L,
                menu = listOf(
                    EndGameMenuModel.UnlockNewCard,
                    EndGameMenuModel.Next,
                    EndGameMenuModel.Menu
                ),
                animateCoins = false,
                showSparkles = true
            )
            assertEquals(expected, sut.uiState.value)
        }

    @Test
    fun `when OnWatchAdClick intent is sent then ShowAd effect and haptic feedback are triggered`() =
        runTest {
            // given
            val rewardedAd = relaxedMockk<RewardedAd>()
            every { allRewardedAds.endGameCoinsAd } returns rewardedAd

            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnWatchAdClick)

                // then
                assertEquals(EndGameEffect.ShowAd(rewardedAd), awaitItem())
                assertEquals(1, hapticFeedback.vibrateLowCount)
            }
        }

    @Test
    fun `when share is clicked then logShareClicked is called`() = runTest {
        // when
        sut.sendIntent(EndGameIntent.OnShareClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logShareClicked(any()) }
    }

    @Test
    fun `when ad reward is earned then logAdRewardFromEndGame is called`() = runTest {
        // given
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L))
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdRewardFromEndGame() }
    }

    @Test
    fun `when OnAdReward intent is sent then coins are rewarded and state is updated`() = runTest {
        sut.uiState.test {
            // given
            skipItems(1)

            // when
            sut.sendIntent(EndGameIntent.OnAdReward)

            // then
            val expectedState = EndGameState(
                rewardedCoins = 0L,
                currentCoins = 0L,
                animateCoins = false,
                menu = listOf(
                    EndGameMenuModel.Next,
                    EndGameMenuModel.Menu
                )
            )
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `when reward coins are ready then current coins include the reward`() = runTest {
        // given
        val boardModel = BoardModel.Grid2x3(isUnlocked = true)
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
        testScheduler.advanceUntilIdle()
        val rewardedCoins = sut.uiState.value.rewardedCoins

        // when
        sut.sendIntent(EndGameIntent.OnRewardCoinsReady)
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertTrue(state.animateCoins)
        assertEquals(rewardedCoins, state.currentCoins)
    }

    @Test
    fun `when daily challenge end game show is sent then state has daily challenge data`() =
        runTest {
            // given
            val boardModel = BoardModel.DAILY_CHALLENGE
            val dailyChallengeModel = DailyChallengeModel(
                epochDay = 100L,
                mistakeCount = 2,
                starCount = 3,
                timeMillis = 5000L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )

            // when
            sut.sendIntent(
                EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
            )
            testScheduler.advanceUntilIdle()

            // then
            val state = sut.uiState.value
            assertTrue(state.isDailyChallenge)
            assertEquals(boardModel, state.board)
            assertEquals(dailyChallengeModel, state.dailyChallenge)
            assertTrue(state.showSparkles)
        }

    @Test
    fun `when next click is sent then haptic feedback is triggered and game screen is opened`() =
        runTest {
            sut.uiEffect.test {
                // given
                val boardModel = BoardModel.Grid2x3(isUnlocked = true)

                // when
                sut.sendIntent(EndGameIntent.OnNextClick(boardModel))

                // then
                assertEquals(EndGameEffect.OpenGameScreen(boardModel), awaitItem())
                assertEquals(1, hapticFeedback.vibrateLowCount)
            }
        }

    @Test
    fun `when menu click is sent then haptic feedback is triggered and menu screen is opened`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnMenuClick)

                // then
                assertEquals(EndGameEffect.OpenMenuScreen, awaitItem())
                assertEquals(1, hapticFeedback.vibrateLowCount)
            }
        }

    @Test
    fun `when menu click is sent in daily challenge mode then menu screen is opened directly`() =
        runTest {
            // given
            val boardModel = BoardModel.DAILY_CHALLENGE
            val dailyChallengeModel = DailyChallengeModel(
                epochDay = 100L,
                mistakeCount = 2,
                starCount = 3,
                timeMillis = 5000L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )
            sut.sendIntent(
                EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
            )
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnMenuClick)

                // then
                assertEquals(EndGameEffect.OpenMenuScreen, awaitItem())
                assertEquals(1, hapticFeedback.vibrateLowCount)
            }
        }

    @Test
    fun `when unlock new card click is sent then haptic feedback is triggered and collection screen is opened`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnUnlockNewCardClick)

                // then
                assertEquals(EndGameEffect.OpenCollectionScreen, awaitItem())
                assertEquals(1, hapticFeedback.vibrateLowCount)
            }
        }

    @Test
    fun `when screen resume is sent then coins are reloaded`() = runTest {
        // given
        val boardModel = BoardModel.Grid2x3(isUnlocked = true)
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
        testScheduler.advanceUntilIdle()
        val coinsAfterReward = userRepository.getCoins().first()

        // when
        sut.sendIntent(EndGameIntent.OnScreenResume)
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertEquals(coinsAfterReward, state.currentCoins)
    }

    @Test
    fun `when daily challenge stars animation finished then coins are rewarded`() = runTest {
        // given
        val boardModel = BoardModel.DAILY_CHALLENGE
        val dailyChallengeModel = DailyChallengeModel(
            epochDay = 100L,
            mistakeCount = 2,
            starCount = 3,
            timeMillis = 5000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
        )
        sut.sendIntent(
            EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
        )
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(EndGameIntent.OnDailyChallengeStarsAnimationFinished)
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        assertTrue(state.rewardedCoins > 0L)
    }

    @Test
    fun `when daily challenge share click is sent then share effect is emitted`() = runTest {
        // given
        val boardModel = BoardModel.DAILY_CHALLENGE
        val dailyChallengeModel = DailyChallengeModel(
            epochDay = 100L,
            mistakeCount = 2,
            starCount = 3,
            timeMillis = 5000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
        )
        sut.sendIntent(
            EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
        )
        testScheduler.advanceUntilIdle()

        sut.uiEffect.test {
            // when
            sut.sendIntent(EndGameIntent.OnDailyChallengeShareClick)

            // then
            assertTrue(awaitItem() is EndGameEffect.ShareDailyChallenge)
        }
    }

    @Test
    fun `when daily challenge share click is sent then analytics is logged`() = runTest {
        // given
        val boardModel = BoardModel.DAILY_CHALLENGE
        val dailyChallengeModel = DailyChallengeModel(
            epochDay = 100L,
            mistakeCount = 2,
            starCount = 3,
            timeMillis = 5000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
        )
        sut.sendIntent(
            EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
        )
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(EndGameIntent.OnDailyChallengeShareClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logDailyChallengeShare(100L, 3) }
    }

    @Test
    fun `when level complete is sent then level complete sound is played`() = runTest {
        // when
        sut.sendIntent(EndGameIntent.OnLevelComplete)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, levelCompletePlayer.playCount)
    }

    @Test
    fun `when ad is dismissed with reward in daily challenge mode then coins are rewarded`() =
        runTest {
            // given
            val boardModel = BoardModel.DAILY_CHALLENGE
            val dailyChallengeModel = DailyChallengeModel(
                epochDay = 100L,
                mistakeCount = 2,
                starCount = 3,
                timeMillis = 5000L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )
            sut.sendIntent(
                EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
            )
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = true))
            testScheduler.advanceUntilIdle()

            // then
            val state = sut.uiState.value
            assertTrue(state.rewardedCoins > 0L)
        }

    @Test
    fun `when ad is dismissed without reward in daily challenge mode then analytics is logged`() =
        runTest {
            // given
            val boardModel = BoardModel.DAILY_CHALLENGE
            val dailyChallengeModel = DailyChallengeModel(
                epochDay = 100L,
                mistakeCount = 2,
                starCount = 3,
                timeMillis = 5000L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )
            sut.sendIntent(
                EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
            )
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = false))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdDismissed("daily_challenge_end_game", false) }
        }

    @Test
    fun `when share click fails to reward then error is logged`() = runTest {
        // given
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L))
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(EndGameIntent.OnShareClick)
        testScheduler.advanceUntilIdle()

        // then
        sut.uiEffect.test {
            assertTrue(awaitItem() is EndGameEffect.Share)
        }
    }

    @Test
    fun `when ad is dismissed without reward in casual mode then analytics is logged`() =
        runTest {
            // given
            val boardModel = BoardModel.Grid2x3(isUnlocked = true)
            sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = false))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdDismissed("end_game", false) }
        }

    @Test
    fun `when next click is sent and notification request should show then OpenEnableNotificationsScreen is sent`() =
        runTest {
            // given
            sut.sendIntent(
                EndGameIntent.OnCasualEndGameShow(
                    BoardModel.Grid2x3(isUnlocked = true),
                    1L
                )
            )
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnNextClick(BoardModel.Grid2x3(isUnlocked = true)))

                // then
                val effect = awaitItem()
                assertTrue(effect is EndGameEffect.OpenEnableNotificationsScreen)
                val notifEffect = effect as EndGameEffect.OpenEnableNotificationsScreen
                assertEquals(EnableNotificationDestination.GAME, notifEffect.destination)
            }
        }

    @Test
    fun `when unlock new card click is sent and notification request should show then OpenEnableNotificationsScreen is sent`() =
        runTest {
            // given
            sut.sendIntent(
                EndGameIntent.OnCasualEndGameShow(
                    BoardModel.Grid2x3(isUnlocked = true),
                    1L
                )
            )
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                sut.sendIntent(EndGameIntent.OnUnlockNewCardClick)

                // then
                val effect = awaitItem()
                assertTrue(effect is EndGameEffect.OpenEnableNotificationsScreen)
                val notifEffect = effect as EndGameEffect.OpenEnableNotificationsScreen
                assertEquals(EnableNotificationDestination.COLLECTION, notifEffect.destination)
            }
        }

    @Test
    fun `when share is clicked and share reward is available then coins are animated`() = runTest {
        // given
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L))
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(EndGameIntent.OnShareClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logShareClicked(true) }
    }

    @Test
    fun `when casual end game show is sent and can unlock new card then menu contains UnlockNewCard`() =
        runTest {
            // given
            userRepository.addCoins(1000)

            // when
            sut.sendIntent(
                EndGameIntent.OnCasualEndGameShow(
                    BoardModel.Grid2x3(isUnlocked = true),
                    1L
                )
            )
            testScheduler.advanceUntilIdle()

            // then
            val menu = sut.uiState.value.menu
            assertTrue(menu.any { it is EndGameMenuModel.UnlockNewCard })
        }

    @Test
    fun `when ad is loaded for daily challenge then menu contains WatchAd`() = runTest {
        // given
        val boardModel = BoardModel.DAILY_CHALLENGE
        val dailyChallengeModel = DailyChallengeModel(
            epochDay = 100L,
            mistakeCount = 2,
            starCount = 3,
            timeMillis = 5000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
        )
        every { allRewardedAds.endGameCoinsAd.loadAndNotify(any(), any()) } answers {
            secondArg<(Boolean) -> Unit>().invoke(true)
        }

        // when
        sut.sendIntent(
            EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
        )
        testScheduler.advanceUntilIdle()

        // then
        val menu = sut.uiState.value.menu
        assertTrue(menu.any { it is EndGameMenuModel.WatchAd })
    }

    @Test
    fun `when watch ad click in daily challenge mode then placement is daily_challenge_end_game`() =
        runTest {
            // given
            val boardModel = BoardModel.DAILY_CHALLENGE
            val dailyChallengeModel = DailyChallengeModel(
                epochDay = 100L,
                mistakeCount = 2,
                starCount = 3,
                timeMillis = 5000L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )
            sut.sendIntent(
                EndGameIntent.OnDailyChallengeEndGameShow(boardModel, dailyChallengeModel)
            )
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnWatchAdClick)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdShown("daily_challenge_end_game") }
        }

    @Test
    fun `when ad reward is earned in casual mode then coins are doubled`() = runTest {
        // given
        sut.sendIntent(EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L))
        testScheduler.advanceUntilIdle()
        val firstReward = sut.uiState.value.rewardedCoins

        // when
        sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        assertTrue(sut.uiState.value.rewardedCoins > firstReward)
        assertTrue(sut.uiState.value.animateRewardCoins)
    }

    @Test
    fun `when ad is dismissed with reward in casual mode then analytics logs ad reward`() =
        runTest {
            // given
            val boardModel = BoardModel.Grid2x3(isUnlocked = true)
            sut.sendIntent(EndGameIntent.OnCasualEndGameShow(boardModel, 1L))
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnAdDismiss(wasRewardGranted = true))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdDismissed("end_game", true) }
        }

    @Test
    fun `when not first game and ad loaded then casual menu contains WatchAd and Share`() =
        runTest {
            // given
            userRepository.incrementTotalGamesPlayed()
            every { allRewardedAds.endGameCoinsAd.loadAndNotify(any(), any()) } answers {
                secondArg<(Boolean) -> Unit>().invoke(true)
            }

            // when
            sut.sendIntent(
                EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L)
            )
            testScheduler.advanceUntilIdle()

            // then
            val menu = sut.uiState.value.menu
            assertTrue(menu.any { it is EndGameMenuModel.WatchAd })
            assertTrue(menu.any { it is EndGameMenuModel.Share })
        }

    @Test
    fun `when share is clicked and reward is granted then coins are animated and menu is updated`() =
        runTest {
            // given
            sut.sendIntent(
                EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L)
            )
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(EndGameIntent.OnShareClick)
            testScheduler.advanceUntilIdle()

            // then
            val state = sut.uiState.value
            assertTrue(state.animateCoins)
        }

    @Test
    fun `when share reward not available then Share menu item shows no reward`() = runTest {
        // given
        userRepository.incrementTotalGamesPlayed()
        userRepository.setHasReceivedShareReward()

        // when
        sut.sendIntent(
            EndGameIntent.OnCasualEndGameShow(BoardModel.Grid2x3(isUnlocked = true), 1L)
        )
        testScheduler.advanceUntilIdle()

        // then
        val shareItem = sut.uiState.value.menu.filterIsInstance<EndGameMenuModel.Share>().first()
        assertEquals(false, shareItem.showReward)
        assertEquals(0L, shareItem.rewardCoins)
    }

}
