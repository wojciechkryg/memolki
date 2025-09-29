package com.wojdor.memolki.ui.feature.endgame

import app.cash.turbine.test
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.RewardedAd
import com.wojdor.memolki.util.media.HapticFeedback
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EndGameViewModelTest : AppTest() {

    private val encryptor = MockEncryptor()
    private val userLocalDataSource = UserLocalDataSource(MockDataStore())
    private val userRepository = UserRepository(encryptor, userLocalDataSource)
    private val hapticFeedback: HapticFeedback = relaxedMockk()
    private val rewardedAds: AllRewardedAds = relaxedMockk()

    private lateinit var sut: EndGameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = EndGameViewModel(
            savedStateHandle = savedStateHandle,
            hapticFeedback = hapticFeedback,
            levelCompletePlayer = relaxedMockk(),
            coinsPlayer = relaxedMockk(),
            rewardedAds = rewardedAds,
            incrementTotalGamesPlayedUseCase = IncrementTotalGamesPlayedUseCase(
                testDispatcher,
                userRepository
            ),
            getCoinsUseCase = GetCoinsUseCase(testDispatcher, userRepository),
            rewardCoinsForLevelUseCase = RewardCoinsForLevelUseCase(
                testDispatcher,
                userRepository
            )
        )
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
            every { rewardedAds.endGameCoinsAd } returns rewardedAd

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
