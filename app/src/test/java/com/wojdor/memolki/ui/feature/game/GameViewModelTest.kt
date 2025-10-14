package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
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
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GameViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var cardFlipPlayer: CardFlipPlayer

    @Inject
    lateinit var cardPairMatchedPlayer: CardPairMatchedPlayer

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @RelaxedMockK
    lateinit var getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase

    @Inject
    lateinit var incrementTotalCardPairsMatchedUseCase: IncrementTotalCardPairsMatchedUseCase

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: GameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = GameViewModel(
            savedStateHandle,
            cardFlipPlayer,
            cardPairMatchedPlayer,
            hapticFeedback,
            getShuffledUnlockedCardsUseCase,
            incrementTotalCardPairsMatchedUseCase,
        )
        every { getShuffledUnlockedCardsUseCase.invoke(LevelModel.Grid2x3()) } returns flowOf(
            Result.success(mockShuffledCardsWithSamePairIds())
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when OnLevelStart intent is sent then state is updated with new level`() = runTest {
        sut.uiState.test {
            // given
            val level = LevelModel.Grid2x3()

            // when
            sut.sendIntent(GameIntent.OnLevelStart(level))

            // then
            assertEquals(LevelModel.Empty, awaitItem().level)
            assertEquals(LevelModel.Grid2x3(), awaitItem().level)
        }
        userRepository.getTotalCardPairsMatched().test {
            assertEquals(0, awaitItem())
        }
    }

    @Test
    fun `when OnCardClick intent is sent then state is updated with flipped card`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
            skipItems(1)

            // when
            val cardToClick = awaitItem().cards[0]
            sut.sendIntent(GameIntent.OnBackCardClick(cardToClick))

            // then
            with(awaitItem().cards[0]) {
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
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                val secondCardToClick = awaitItem().cards[2]
                sut.sendIntent(GameIntent.OnBackCardClick(secondCardToClick))

                // then
                val result = awaitItem()
                with(result.cards[0]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[2]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(0, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with already flipped one cards to front then state is updated with the new flipped card and they match`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(1)

                // then
                val result = awaitItem()
                with(result.cards[2]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[3]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                skipItems(1)
                assertEquals(1, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with already flipped two cards to front then state is updated with the new flipped card`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                skipItems(1)
                val thirdCardToClick = awaitItem().cards[3]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))

                // then
                val result = awaitItem()
                with(result.cards[0]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[2]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[3]) {
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
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                val thirdCardToClick = awaitItem().cards[3]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))
                skipItems(1)

                // then
                val result = awaitItem()
                with(result.cards[0]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[1]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[2]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(1, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with two cards that matches because of same id then state is updated matched cards`() =
        runTest {
            sut.uiState.test {
                // given
                every { getShuffledUnlockedCardsUseCase.invoke(LevelModel.Grid2x3()) } returns flowOf(
                    Result.success(mockShuffledCardsWithSameIds())
                )
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(1)

                // then
                val result = awaitItem()
                with(result.cards[0]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
                with(result.cards[1]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[2]) {
                    assertFalse(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[3]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                skipItems(1)
                assertEquals(1, awaitItem())
            }
        }

    @Test
    fun `when OnCardClick intent is sent with two cards that didn't match then state is updated accordingly`() =
        runTest {
            sut.uiState.test {
                // given
                every { getShuffledUnlockedCardsUseCase.invoke(LevelModel.Grid2x3()) } returns flowOf(
                    Result.success(mockShuffledCardsWithSameIds())
                )
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[7]))

                // then
                val result = awaitItem()
                with(result.cards[1]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
                with(result.cards[7]) {
                    assertTrue(isFlippedFront)
                    assertFalse(isPairMatched)
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(0, awaitItem())
            }
        }

    @Test
    fun `when all cards are matched then the OpenEndGameScreen effect is sent`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLevelStart(LevelModel.Grid2x3()))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
                skipItems(5)

                // then
                val result = awaitItem()
                assertTrue(result.cards.all { it.isFlippedFront && it.isPairMatched })
                sut.uiEffect.test {
                    skipItems(1)
                    val effect = awaitItem()
                    assertEquals(
                        GameEffect.OpenEndGameScreen(LevelModel.Grid2x3()),
                        effect
                    )
                }
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(3, awaitItem())
            }
        }

    private fun mockShuffledCardsWithSamePairIds(): List<CardModel> {
        return listOf(
            CardModel.Image("banana_whole", "banana", 1, 1),
            CardModel.Image("banana_half", "banana", 1, 1),
            CardModel.Image("apple_whole", "apple", 2, 2),
            CardModel.Text("apple_half", "apple", 2),
            CardModel.Text("strawberry_whole", "strawberry", 3),
            CardModel.Text("strawberry_half", "strawberry", 3)
        )
    }

    private fun mockShuffledCardsWithSameIds(): List<CardModel> {
        return listOf(
            CardModel.Image("john_snow", "john_snow", 1, 1),
            CardModel.Image("stark", "john_snow", 1, 1),
            CardModel.Image("arya_stark", "arya_stark", 2, 2),
            CardModel.Image("stark", "arya_stark", 1, 1),
            CardModel.Text("sansa_stark", "sansa_stark", 3),
            CardModel.Image("stark", "sansa_stark", 1, 1),
            CardModel.Text("daenerys_targaryen", "daenerys_targaryen", 4),
            CardModel.Image("targaryen", "daenerys_targaryen", 1, 1),
        )
    }
}
