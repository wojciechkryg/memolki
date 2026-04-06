package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.CanUnlockNewCardUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.HasReceivedShareRewardUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForBoardUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShareUseCase
import com.wojdor.memolki.domain.usecase.ShouldShowNotificationRequestUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.formatter.CasualShareFormatter
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.playgames.GooglePlayGames
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EndGameViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var levelCompletePlayer: LevelCompletePlayer

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var coinsPlayer: CoinsPlayer

    @Inject
    lateinit var allRewardedAds: AllRewardedAds

    @Inject
    lateinit var reviewManager: ReviewManager

    @Inject
    lateinit var googlePlayGames: GooglePlayGames

    @Inject
    lateinit var incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase

    @Inject
    lateinit var getCoinsUseCase: GetCoinsUseCase

    @Inject
    lateinit var rewardCoinsForBoardUseCase: RewardCoinsForBoardUseCase

    @Inject
    lateinit var getTotalCoinsUseCase: GetTotalCoinsUseCase

    @Inject
    lateinit var canUnlockNewCardUseCase: CanUnlockNewCardUseCase

    @Inject
    lateinit var shouldShowNotificationRequestUseCase: ShouldShowNotificationRequestUseCase

    @Inject
    lateinit var rewardCoinsForShareUseCase: RewardCoinsForShareUseCase

    @Inject
    lateinit var hasReceivedShareRewardUseCase: HasReceivedShareRewardUseCase

    @Inject
    lateinit var getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var casualShareFormatter: CasualShareFormatter

    @Inject
    lateinit var dailyChallengeShareFormatter: DailyChallengeShareFormatter

    private lateinit var sut: EndGameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = EndGameViewModel(
            savedStateHandle,
            analytics,
            levelCompletePlayer,
            coinsPlayer,
            hapticFeedback,
            allRewardedAds,
            reviewManager,
            googlePlayGames,
            incrementTotalGamesPlayedUseCase,
            getTotalGamesPlayedUseCase,
            getCoinsUseCase,
            rewardCoinsForBoardUseCase,
            getTotalCoinsUseCase,
            canUnlockNewCardUseCase,
            shouldShowNotificationRequestUseCase,
            rewardCoinsForShareUseCase,
            hasReceivedShareRewardUseCase,
            casualShareFormatter,
            dailyChallengeShareFormatter
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
                    EndGameMenuModel.Next,
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.Share(
                        showReward = true,
                        rewardCoins = RewardCoinsForShareUseCase.SHARE_REWARD_COINS
                    )
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
                verify { hapticFeedback.vibrateLow() }
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
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.Share(
                        showReward = false,
                        rewardCoins = 0L
                    )
                )
            )
            assertEquals(expectedState, awaitItem())
        }
    }

}
