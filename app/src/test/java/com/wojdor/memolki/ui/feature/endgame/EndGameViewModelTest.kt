package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
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
    lateinit var incrementTotalGamesPlayedUseCase: IncrementTotalGamesPlayedUseCase

    @Inject
    lateinit var getCoinsUseCase: GetCoinsUseCase

    @Inject
    lateinit var rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase

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
            userRepository,
            incrementTotalGamesPlayedUseCase,
            getCoinsUseCase,
            rewardCoinsForLevelUseCase
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
                skipItems(1)

                // when
                sut.sendIntent(EndGameIntent.OnEndGameShow(levelModel))
                skipItems(2)

                // then
                val expected = EndGameState(
                    level = levelModel,
                    rewardedCoins = rewardedCoins,
                    currentCoins = rewardedCoins,
                    menu = listOf(
                        EndGameMenuModel.PlayAgain,
                        EndGameMenuModel.Menu
                    ),
                    animateCoins = true
                )
                assertEquals(expected, awaitItem())
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
                    EndGameMenuModel.Menu
                )
            )
            assertEquals(expectedState, awaitItem())
        }
    }
}
