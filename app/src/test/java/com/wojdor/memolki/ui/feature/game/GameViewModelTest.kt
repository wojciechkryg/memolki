package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetLevelUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.IncrementLevelUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.GetBiggestUnlockedBoardUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.TimeProvider
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
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

    @Inject
    lateinit var googlePlayGames: GooglePlayGames

    @RelaxedMockK
    lateinit var getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase

    @Inject
    lateinit var incrementTotalCardPairsMatchedUseCase: IncrementTotalCardPairsMatchedUseCase

    @Inject
    lateinit var getLevelUseCase: GetLevelUseCase

    @Inject
    lateinit var incrementLevelUseCase: IncrementLevelUseCase

    @Inject
    lateinit var getBiggestUnlockedBoardUseCase: GetBiggestUnlockedBoardUseCase

    @Inject
    lateinit var getDailyChallengeCardsUseCase: GetDailyChallengeCardsUseCase

    @Inject
    lateinit var hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase

    @Inject
    lateinit var saveDailyChallengeUseCase: SaveDailyChallengeUseCase

    @Inject
    lateinit var getTodayDailyChallengeUseCase: GetTodayDailyChallengeUseCase

    @Inject
    lateinit var timeProvider: TimeProvider

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var analytics: Analytics

    private lateinit var sut: GameViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = GameViewModel(
            savedStateHandle,
            analytics,
            cardFlipPlayer,
            cardPairMatchedPlayer,
            hapticFeedback,
            googlePlayGames,
            getShuffledUnlockedCardsUseCase,
            incrementTotalCardPairsMatchedUseCase,
            getLevelUseCase,
            incrementLevelUseCase,
            getBiggestUnlockedBoardUseCase,
            getDailyChallengeCardsUseCase,
            hasPlayedTodayDailyChallengeUseCase,
            saveDailyChallengeUseCase,
            getTodayDailyChallengeUseCase,
            timeProvider
        )
        every { getShuffledUnlockedCardsUseCase.invoke(any()) } returns flowOf(
            Result.success(mockShuffledCardsWithSamePairIds())
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when OnBoardStart intent is sent then state is updated with new board`() = runTest {
        sut.uiState.test {
            // when
            sut.sendIntent(GameIntent.OnBoardStart("2x3"))

            // then
            assertEquals(BoardModel.Empty, awaitItem().board)
            assertEquals(BoardModel.Grid2x3(isUnlocked = true), awaitItem().board)
        }
        userRepository.getTotalCardPairsMatched().test {
            assertEquals(0, awaitItem())
        }
    }

    @Test
    fun `when OnCardClick intent is sent then state is updated with flipped card`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnBoardStart("2x3"))
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
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                val secondCardToClick = awaitItem().cards[2]
                sut.sendIntent(GameIntent.OnBackCardClick(secondCardToClick))

                // then
                skipItems(1)
                val result = awaitItem()
                with(result.cards[0]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isMistakeShaking)
                    assertFalse(isPairMatched)
                }
                with(result.cards[2]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isMistakeShaking)
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
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
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
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                skipItems(1)
                val thirdCardToClick = awaitItem().cards[3]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))
                skipItems(1)

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
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                val thirdCardToClick = awaitItem().cards[3]
                sut.sendIntent(GameIntent.OnBackCardClick(thirdCardToClick))
                skipItems(3)

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
                assertFalse(result.shouldShowCardText)
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
                every { getShuffledUnlockedCardsUseCase.invoke(any()) } returns flowOf(
                    Result.success(mockShuffledCardsWithSameIds())
                )
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(2)

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
                assertTrue(result.shouldShowCardText)
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
                every { getShuffledUnlockedCardsUseCase.invoke(any()) } returns flowOf(
                    Result.success(mockShuffledCardsWithSameIds())
                )
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)


                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[7]))

                // then
                skipItems(1)
                val result = awaitItem()
                with(result.cards[1]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isMistakeShaking)
                    assertFalse(isPairMatched)
                }
                with(result.cards[7]) {
                    assertTrue(isFlippedFront)
                    assertTrue(isMistakeShaking)
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
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)

                // when
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
                skipItems(6)

                // then
                val result = awaitItem()
                assertTrue(result.cards.all { it.isFlippedFront && it.isPairMatched })
                sut.uiEffect.test {
                    skipItems(1)
                    assertTrue(awaitItem() is GameEffect.SendTotalCardPairsMatchedScore)
                    skipItems(1)
                    val endGameEffect = awaitItem() as GameEffect.OpenEndGameScreen
                    assertEquals(BoardModel.Grid2x3(isUnlocked = true), endGameEffect.boardModel)
                    assertEquals(0, endGameEffect.mistakeCount)
                    assertEquals(3, endGameEffect.cardFlipCounts.size)
                }
                skipItems(2)
            }
            userRepository.getTotalCardPairsMatched().test {
                assertEquals(3, awaitItem())
            }
        }

    @Test
    fun `when board starts then logBoardStart is called`() = runTest {
        // given
        // when
        sut.sendIntent(GameIntent.OnBoardStart("2x3"))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logBoardStart(BoardModel.Grid2x3(isUnlocked = true)) }
    }

    @Test
    fun `when all cards matched then logBoardComplete is called with mismatch count`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnBoardStart("2x3"))
            skipItems(1)

            // when
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
            cancelAndIgnoreRemainingEvents()
        }
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logBoardComplete(BoardModel.Grid2x3(isUnlocked = true), 0) }
    }

    @Test
    fun `when game is left without finishing then logBoardAbandon is called`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnBoardStart("2x3"))
            skipItems(1)
            awaitItem()

            // when
            sut.sendIntent(GameIntent.OnGameLeave)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logBoardAbandoned(BoardModel.Grid2x3(isUnlocked = true)) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when game is left after finishing then logBoardAbandon is not called`() = runTest {
        sut.uiState.test {
            // given
            sut.sendIntent(GameIntent.OnBoardStart("2x3"))
            skipItems(1)
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
            sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
            cancelAndIgnoreRemainingEvents()
        }

        // when
        sut.sendIntent(GameIntent.OnGameLeave)

        // then
        verify(exactly = 0) { analytics.logBoardAbandoned(any()) }
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
