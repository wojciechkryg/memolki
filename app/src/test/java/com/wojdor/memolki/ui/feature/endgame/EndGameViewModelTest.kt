package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.CanUnlockNewCardUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShareUseCase
import com.wojdor.memolki.domain.usecase.ShouldShowNotificationRequestUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
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
    lateinit var rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase

    @Inject
    lateinit var getTotalCoinsUseCase: GetTotalCoinsUseCase

    @Inject
    lateinit var canUnlockNewCardUseCase: CanUnlockNewCardUseCase

    @Inject
    lateinit var shouldShowNotificationRequestUseCase: ShouldShowNotificationRequestUseCase

    @Inject
    lateinit var rewardCoinsForShareUseCase: RewardCoinsForShareUseCase

    @Inject
    lateinit var checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: EndGameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = EndGameViewModel(
            savedStateHandle,
            levelCompletePlayer,
            coinsPlayer,
            hapticFeedback,
            allRewardedAds,
            reviewManager,
            googlePlayGames,
            userRepository,
            incrementTotalGamesPlayedUseCase,
            getCoinsUseCase,
            rewardCoinsForLevelUseCase,
            getTotalCoinsUseCase,
            canUnlockNewCardUseCase,
            shouldShowNotificationRequestUseCase,
            rewardCoinsForShareUseCase,
            checkDailyLoginStreakUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when OnEndGameShow intent is sent then the state is updated with the level and rewarded coins`() =
        runTest {
            sut.uiState.test {
                // given
                val levelModel = LevelModel.Grid2x3(isUnlocked = true)
                val rewardedCoins = 1L

                // when
                sut.sendIntent(EndGameIntent.OnEndGameShow(levelModel))

                // then
                val expected = EndGameState(
                    level = levelModel,
                    rewardedCoins = rewardedCoins,
                    currentCoins = rewardedCoins,
                    menu = listOf(
                        EndGameMenuModel.PlayAgain,
                        EndGameMenuModel.Menu,
                        EndGameMenuModel.FreeCoins,
                        EndGameMenuModel.Share(
                            showReward = true,
                            rewardCoins = RewardCoinsForShareUseCase.SHARE_REWARD_COINS
                        )
                    ),
                    animateCoins = true,
                    showSparkles = true
                )
                var lastItem = awaitItem()
                while (lastItem != expected) {
                    lastItem = awaitItem()
                }
                assertEquals(expected, lastItem)
            }
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
                    EndGameMenuModel.PlayAgain,
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
