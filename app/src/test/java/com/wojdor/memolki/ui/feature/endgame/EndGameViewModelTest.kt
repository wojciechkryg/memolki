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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EndGameViewModelTest : AppTest() {

    private val encryptor = MockEncryptor()
    private val userLocalDataSource = UserLocalDataSource(MockDataStore())
    private val userRepository = UserRepository(encryptor, userLocalDataSource)

    private lateinit var sut: EndGameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = EndGameViewModel(
            savedStateHandle = savedStateHandle,
            incrementTotalGamesPlayedUseCase = IncrementTotalGamesPlayedUseCase(
                testDispatcher,
                userRepository
            ),
            getCoinsUseCase = GetCoinsUseCase(testDispatcher, userRepository),
            rewardCoinsForLevelUseCase = RewardCoinsForLevelUseCase(
                testDispatcher,
                userRepository
            ),
            hapticFeedback = relaxedMockk(),
            levelCompletePlayer = relaxedMockk(),
            coinsPlayer = relaxedMockk()
        )
    }

    @Test
    fun `when OnEndGameShow intent is sent then the state is updated with the level and rewarded coins`() =
        runTest {
            sut.uiState.test {
                // given
                val levelModel = LevelModel.Grid2x3()
                val rewardedCoins = 1L
                skipItems(1)

                // when
                sut.sendIntent(EndGameIntent.OnEndGameShow(levelModel))

                // then
                assertEquals(
                    EndGameState(
                        level = levelModel,
                        menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu)
                    ), awaitItem()
                )
                val expected = EndGameState(
                    level = levelModel,
                    rewardedCoins = rewardedCoins,
                    currentCoins = rewardedCoins,
                    menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu),
                    animateCoins = true
                )
                assertEquals(expected, awaitItem())
            }
        }
}
