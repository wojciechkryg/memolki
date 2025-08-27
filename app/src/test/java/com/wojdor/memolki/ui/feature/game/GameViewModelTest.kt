package com.wojdor.memolki.ui.feature.game

import app.cash.turbine.test
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest : AppTest() {

    @RelaxedMockK
    private lateinit var getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase

    private val userRepository = UserRepository(
        encryptor = MockEncryptor(),
        userLocalDataSource = UserLocalDataSource(MockDataStore())
    )
    private val incrementTotalCardPairsMatchedUseCase = IncrementTotalCardPairsMatchedUseCase(
        testDispatcher,
        userRepository
    )

    private lateinit var sut: GameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = GameViewModel(
            savedStateHandle,
            getShuffledUnlockedCardsUseCase,
            incrementTotalCardPairsMatchedUseCase
        )
        every { getShuffledUnlockedCardsUseCase.invoke(LevelModel.Grid2x3) } returns flowOf(
            Result.success(mockShuffledCards())
        )
    }

    @Test
    fun `when OnLevelStart intent is sent then state is updated with new level`() = runTest {
        sut.uiState.test {
            // given
            val level = LevelModel.Grid2x3

            // when
            sut.sendIntent(GameIntent.OnLevelStart(level))

            // then
            assertEquals(LevelModel.Empty, awaitItem().level)
            assertEquals(LevelModel.Grid2x3, awaitItem().level)
        }
        userRepository.getTotalCardPairsMatched().test {
            assertEquals(0, awaitItem())
        }
    }

    @Test
    fun `when OnCardClick intent is sent then state is updated with flipped card`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
            skipItems(1)

            // when
            val cardToClick = awaitItem().cards[0][0]
            sut.sendIntent(GameIntent.OnBackCardClick(cardToClick))

            // then
            with(awaitItem().cards[0][0]) {
                assertTrue(isFlippedFront)
                assertFalse(isPairMatched)
            }
        }
        userRepository.getTotalCardPairsMatched().test {
            assertEquals(0, awaitItem())
        }
    }

    @Test
    fun `when OnCardClick intent is sent with already flipped to front card then state is updated with no flipped card`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][0]))
                val secondCardToClick = awaitItem().cards[1][0]
                sut.sendIntent(GameIntent.OnBackCardClick(secondCardToClick))

                // then
                val result = awaitItem()
                with(result.cards[0][0]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[1][0]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(0, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with already flipped two cards to front then state is updated with the new flipped card`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1][0]))
                val thirdCardToClick = awaitItem().cards[1][1]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))
                skipItems(1)

                // then
                val result = awaitItem()
                with(result.cards[0][0]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[1][0]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[1][1]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(0, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with already flipped two cards to front card and they match then state is updated matched cards`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][1]))
                val thirdCardToClick = awaitItem().cards[1][1]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))
                skipItems(1)

                // then
                val result = awaitItem()
                with(result.cards[0][0]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[0][1]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[1][0]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(1, awaitItem())
            }
        }

    @Test
    fun `when all cards are matched then the OpenEndGameScreen effect is sent`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0][1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1][0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1][1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2][0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2][1]))
                skipItems(3)

                // then
                val result = awaitItem()
                assertTrue(result.cards.flatten().all { it.isFlippedFront && it.isPairMatched })
                sut.uiEffect.test {
                    skipItems(1)
                    val effect = awaitItem()
                    assertEquals(
                        GameEffect.OpenEndGameScreen(LevelModel.Grid2x3),
                        effect
                    )
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(3, awaitItem())
            }
        }

    private fun mockShuffledCards(): List<List<CardModel>> {
        return listOf(
            CardModel.Image("banana_whole", "banana", 1, 1),
            CardModel.Image("banana_half", "banana", 1, 1),
            CardModel.Image("apple_whole", "apple", 2, 2),
            CardModel.Text("apple_half", "apple", 2),
            CardModel.Text("strawberry_whole", "strawberry", 3),
            CardModel.Text("strawberry_half", "strawberry", 3)
        ).chunked(2)
    }
}
