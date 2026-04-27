package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.empty

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.StarCalculator
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAnalytics
import com.wojdor.memolki.test.fake.FakeCardPairMatchedPlayer
import com.wojdor.memolki.test.fake.FakeGetDailyChallengeCardsUseCase
import com.wojdor.memolki.test.fake.FakeGetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.test.fake.FakeGetTodayDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import com.wojdor.memolki.test.fake.FakeHasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeSaveDailyChallengeUseCase
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GameViewModelTest : AppTest() {

    private val hapticFeedback: FakeHapticFeedback by inject()
    private val cardPairMatchedPlayer: FakeCardPairMatchedPlayer by inject()
    private val timeProvider: TimeProvider by inject()
    private val userRepository: UserRepository by inject()
    private val analytics: FakeAnalytics by inject()
    private val coroutineDispatcher: CoroutineDispatcher by inject()
    private val cardRepository: CardRepository by inject()
    private val dailyChallengeRepository: DailyChallengeRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    private val packageNameProvider: PackageNameProvider by inject()
    private val random: Random by inject()

    private lateinit var getShuffledUnlockedCardsUseCase: FakeGetShuffledUnlockedCardsUseCase
    private lateinit var getDailyChallengeCardsUseCase: FakeGetDailyChallengeCardsUseCase
    private lateinit var hasPlayedTodayDailyChallengeUseCase: FakeHasPlayedTodayDailyChallengeUseCase
    private lateinit var saveDailyChallengeUseCase: FakeSaveDailyChallengeUseCase
    private lateinit var getTodayDailyChallengeUseCase: FakeGetTodayDailyChallengeUseCase

    private lateinit var sut: GameViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        getShuffledUnlockedCardsUseCase = FakeGetShuffledUnlockedCardsUseCase(
            coroutineDispatcher, cardRepository, random
        )
        getDailyChallengeCardsUseCase = FakeGetDailyChallengeCardsUseCase(
            coroutineDispatcher, cardRepository, timeProvider, packageNameProvider
        )
        hasPlayedTodayDailyChallengeUseCase = FakeHasPlayedTodayDailyChallengeUseCase(
            coroutineDispatcher, dailyChallengeRepository, timeProvider
        )
        saveDailyChallengeUseCase = FakeSaveDailyChallengeUseCase(
            coroutineDispatcher, dailyChallengeRepository, notificationRepository, timeProvider
        )
        getTodayDailyChallengeUseCase = FakeGetTodayDailyChallengeUseCase(
            coroutineDispatcher, dailyChallengeRepository, timeProvider
        )
        loadKoinModules(
            module {
                factory<GetShuffledUnlockedCardsUseCase> { getShuffledUnlockedCardsUseCase }
                factory<GetDailyChallengeCardsUseCase> { getDailyChallengeCardsUseCase }
                factory<HasPlayedTodayDailyChallengeUseCase> { hasPlayedTodayDailyChallengeUseCase }
                factory<SaveDailyChallengeUseCase> { saveDailyChallengeUseCase }
                factory<GetTodayDailyChallengeUseCase> { getTodayDailyChallengeUseCase }
            }
        )
        getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSamePairIds())
        sut = get()
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
                getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSameIds())
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
                getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSameIds())
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
                skipItems(5)

                // then
                val result = awaitItem()
                assertTrue(result.cards.all { it.isFlippedFront && it.isPairMatched })
                sut.sendIntent(GameIntent.OnMatchAnimationComplete)
                sut.uiEffect.test {
                    skipItems(1)
                    assertTrue(awaitItem() is GameEffect.SendTotalCardPairsMatchedScore)
                    skipItems(2)
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
        assertTrue(analytics.lastBoardStart is BoardModel.Grid2x3)
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
        val (board, mistakeCount) = analytics.lastBoardComplete!!
        assertTrue(board is BoardModel.Grid2x3)
        assertEquals(0, mistakeCount)
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
            assertTrue(analytics.lastBoardAbandoned is BoardModel.Grid2x3)
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
        assertNull(analytics.lastBoardAbandoned)
    }

    @Test
    fun `when match animation completes twice then OpenEndGameScreen effect is sent only once`() =
        runTest {
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
                skipItems(5)
                awaitItem()

                // when
                sut.sendIntent(GameIntent.OnMatchAnimationComplete)
                sut.sendIntent(GameIntent.OnMatchAnimationComplete)

                // then
                sut.uiEffect.test {
                    skipItems(1)
                    assertTrue(awaitItem() is GameEffect.SendTotalCardPairsMatchedScore)
                    skipItems(2)
                    val endGameEffect = awaitItem() as GameEffect.OpenEndGameScreen
                    assertEquals(BoardModel.Grid2x3(isUnlocked = true), endGameEffect.boardModel)
                    expectNoEvents()
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when OnBoardStart with isDailyChallenge true then state is updated with daily challenge board`() =
        runTest {
            // given
            val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

            sut.uiState.test {
                // when
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))

                // then
                assertEquals(BoardModel.Empty, awaitItem().board)
                val state = awaitItem()
                assertEquals(BoardModel.DAILY_CHALLENGE, state.board)
                assertTrue(state.isDailyChallenge)
                assertEquals(dailyChallengeCards.size, state.cards.size)
            }
        }

    @Test
    fun `when OnBoardStart with isDailyChallenge true then logDailyChallengeStart is called`() =
        runTest {
            // given
            val epochDay = LocalDate(2026, 3, 26).toEpochDays()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(mockShuffledCardsWithSamePairIds())

            // when
            sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(epochDay, analytics.lastDailyChallengeStart)
        }

    @Test
    fun `when OnFrontCardPress with isPressed true on matched Image card then card details are shown`() =
        runTest {
            sut.uiState.test {
                // given
                getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSameIds())
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(2)
                val stateAfterMatch = awaitItem()
                val matchedImageCard = stateAfterMatch.cards[0]
                assertTrue(matchedImageCard.isPairMatched)
                assertTrue(matchedImageCard is CardModel.Image)

                // when
                sut.sendIntent(GameIntent.OnFrontCardPress(isPressed = true, matchedImageCard))
                skipItems(1)

                // then
                val result = awaitItem()
                assertTrue(result.shouldShowCardDetails)
                assertEquals(matchedImageCard, result.lastCardPressed)
            }
        }

    @Test
    fun `when OnFrontCardPress with isPressed true on matched Image card then haptic is triggered`() =
        runTest {
            sut.uiState.test {
                // given
                getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSameIds())
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(2)
                val matchedImageCard = awaitItem().cards[0]

                // when
                sut.sendIntent(GameIntent.OnFrontCardPress(isPressed = true, matchedImageCard))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(1, hapticFeedback.vibrateLowCount)
        }

    @Test
    fun `when OnFrontCardPress with isPressed false then card details are hidden`() =
        runTest {
            sut.uiState.test {
                // given
                getShuffledUnlockedCardsUseCase.result = Result.success(mockShuffledCardsWithSameIds())
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                skipItems(2)
                val matchedCard = awaitItem().cards[0]
                sut.sendIntent(GameIntent.OnFrontCardPress(isPressed = true, matchedCard))
                skipItems(1)
                val stateWithDetails = awaitItem()
                assertTrue(stateWithDetails.shouldShowCardDetails)

                // when
                sut.sendIntent(GameIntent.OnFrontCardPress(isPressed = false, matchedCard))

                // then
                val result = awaitItem()
                assertFalse(result.shouldShowCardDetails)
            }
        }

    @Test
    fun `when OnMatchAnimationComplete with no cards animating then state is unchanged`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)
                awaitItem()

                // when
                sut.sendIntent(GameIntent.OnMatchAnimationComplete)
                testScheduler.advanceUntilIdle()

                // then
                expectNoEvents()
            }
        }

    @Test
    fun `when game is left during daily challenge then logDailyChallengeAbandoned is called`() =
        runTest {
            // given
            val epochDay = LocalDate(2026, 3, 26).toEpochDays()
            val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

            sut.uiState.test {
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
                skipItems(1)
                awaitItem()

                // when
                sut.sendIntent(GameIntent.OnGameLeave)
                testScheduler.advanceUntilIdle()

                // then
                assertEquals(epochDay, analytics.lastDailyChallengeAbandoned)
                assertNull(analytics.lastBoardAbandoned)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when daily challenge completed with 0 mistakes then 3 stars are awarded`() =
        runTest {
            // given
            val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

            sut.uiState.test {
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
                skipItems(1)

                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            assertEquals(0, sut.uiState.value.mistakeCount)
            assertTrue(sut.uiState.value.isGameFinished)

            // when
            sut.sendIntent(GameIntent.OnMatchAnimationComplete)
            testScheduler.advanceUntilIdle()

            // then
            sut.uiEffect.test {
                skipItems(3)
                assertTrue(awaitItem() is GameEffect.OnPairMatched)
                val endGameEffect = awaitItem() as GameEffect.OpenEndGameScreen
                assertEquals(BoardModel.DAILY_CHALLENGE, endGameEffect.boardModel)
                assertEquals(0, endGameEffect.mistakeCount)
                assertEquals(StarCalculator.MAX_STARS, endGameEffect.dailyChallenge.starCount)
            }
        }

    @Test
    fun `when daily challenge completed with 1 mistake then 2 stars are awarded`() =
        runTest {
            // given
            val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

            sut.uiState.test {
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
                skipItems(1)
                val cards = awaitItem().cards
                sut.sendIntent(GameIntent.OnBackCardClick(cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(cards[2]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            sut.uiState.test {
                awaitItem()
                sut.sendIntent(GameIntent.OnBackCardClick(dailyChallengeCards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            sut.uiState.test {
                awaitItem()
                sut.sendIntent(GameIntent.OnBackCardClick(dailyChallengeCards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            val mistakeCount = sut.uiState.value.mistakeCount
            assertTrue(mistakeCount in 1..4, "Expected 1-4 mistakes, got $mistakeCount")
            assertTrue(sut.uiState.value.isGameFinished)

            // when
            sut.sendIntent(GameIntent.OnMatchAnimationComplete)
            testScheduler.advanceUntilIdle()

            // then
            sut.uiEffect.test {
                skipItems(3)
                assertTrue(awaitItem() is GameEffect.OnPairMatched)
                val endGameEffect = awaitItem() as GameEffect.OpenEndGameScreen
                assertEquals(StarCalculator.TWO_STARS, endGameEffect.dailyChallenge.starCount)
            }
        }

    @Test
    fun `when daily challenge completed with 5 or more mistakes then 1 star is awarded`() =
        runTest {
            // given
            val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
            )
            saveDailyChallengeUseCase.result = Result.success(Unit)
            getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

            sut.uiState.test {
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
                skipItems(1)
                val cards = awaitItem().cards
                sut.sendIntent(GameIntent.OnBackCardClick(cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(cards[2]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            repeat(4) {
                sut.uiState.test {
                    awaitItem()
                    sut.sendIntent(GameIntent.OnBackCardClick(dailyChallengeCards[0]))
                    sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                    cancelAndIgnoreRemainingEvents()
                }
                testScheduler.advanceUntilIdle()
            }

            sut.uiState.test {
                awaitItem()
                sut.sendIntent(GameIntent.OnBackCardClick(dailyChallengeCards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[1]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[3]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[4]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[5]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            assertTrue(
                sut.uiState.value.mistakeCount >= 5,
                "Expected 5+ mistakes, got ${sut.uiState.value.mistakeCount}"
            )
            assertTrue(sut.uiState.value.isGameFinished)

            // when
            sut.sendIntent(GameIntent.OnMatchAnimationComplete)
            testScheduler.advanceUntilIdle()

            // then
            sut.uiEffect.test {
                skipItems(3)
                assertTrue(awaitItem() is GameEffect.OnPairMatched)
                val endGameEffect = awaitItem() as GameEffect.OpenEndGameScreen
                assertEquals(StarCalculator.MIN_STARS, endGameEffect.dailyChallenge.starCount)
            }
        }

    @Test
    fun `when same card is flipped twice then mistake count increases`() =
        runTest {
            // given
            sut.uiState.test {
                sut.sendIntent(GameIntent.OnBoardStart("2x3"))
                skipItems(1)
                val cards = awaitItem().cards
                sut.sendIntent(GameIntent.OnBackCardClick(cards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(cards[2]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            val originalCards = mockShuffledCardsWithSamePairIds()

            // when
            sut.uiState.test {
                awaitItem()
                sut.sendIntent(GameIntent.OnBackCardClick(originalCards[0]))
                sut.sendIntent(GameIntent.OnBackCardClick(awaitItem().cards[2]))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            assertTrue(
                sut.uiState.value.mistakeCount > 0,
                "Expected mistake count > 0, got ${sut.uiState.value.mistakeCount}"
            )
        }

    @Test
    fun `when daily challenge already played then OpenEndGameScreen is sent with today challenge data`() =
        runTest {
            // given
            val todayChallenge = DailyChallengeModel(
                mistakeCount = 3,
                starCount = 2,
                timeMillis = 12000L,
                epochDay = 0L,
                cardFlipCounts = listOf(listOf(1, 2), listOf(3, 1))
            )
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(true
            )
            getTodayDailyChallengeUseCase.result = Result.success(todayChallenge
            )

            sut.uiEffect.test {
                // when
                sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
                testScheduler.advanceUntilIdle()

                // then
                val effect = awaitItem() as GameEffect.OpenEndGameScreen
                assertEquals(BoardModel.DAILY_CHALLENGE, effect.boardModel)
                assertEquals(3, effect.mistakeCount)
                assertEquals(todayChallenge.cardFlipCounts, effect.cardFlipCounts)
            }
        }

    @Test
    fun `when daily challenge already played then logDailyChallengeAlreadyPlayed is called`() =
        runTest {
            // given
            hasPlayedTodayDailyChallengeUseCase.result = Result.success(true
            )
            getTodayDailyChallengeUseCase.result = Result.success(
                    DailyChallengeModel(
                        mistakeCount = 0,
                        starCount = 3,
                        timeMillis = 5000L,
                        epochDay = 0L,
                        cardFlipCounts = listOf(listOf(1)
                    )
                )
            )

            // when
            sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
            testScheduler.advanceUntilIdle()

            // then
            assertNotNull(analytics.lastDailyChallengeAlreadyPlayed)
        }

    @Test
    fun `when daily challenge start with cards already loaded then does nothing`() = runTest {
        // given
        val dailyChallengeCards = mockShuffledCardsWithSamePairIds()
        hasPlayedTodayDailyChallengeUseCase.result = Result.success(false
        )
        saveDailyChallengeUseCase.result = Result.success(Unit)
        getDailyChallengeCardsUseCase.result = Result.success(dailyChallengeCards)

        sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
        testScheduler.advanceUntilIdle()
        assertTrue(sut.uiState.value.cards.isNotEmpty())

        // when
        sut.sendIntent(GameIntent.OnBoardStart("", isDailyChallenge = true))
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, hasPlayedTodayDailyChallengeUseCase.invocationCount)
    }

    @Test
    fun `when play match sound then card pair matched player is played`() = runTest {
        // when
        sut.playMatchSound()

        // then
        assertEquals(1, cardPairMatchedPlayer.playCount)
    }

    @Test
    fun `when OnLeaveConfirmationShow intent is sent then shouldShowLeaveConfirmation is true`() =
        runTest {
            sut.uiState.test {
                // when
                sut.sendIntent(GameIntent.OnLeaveConfirmationShow)

                // then
                assertFalse(awaitItem().shouldShowLeaveConfirmation)
                assertTrue(awaitItem().shouldShowLeaveConfirmation)
            }
        }

    @Test
    fun `when OnLeaveConfirmationDismiss intent is sent then shouldShowLeaveConfirmation is false`() =
        runTest {
            sut.uiState.test {
                // given
                sut.sendIntent(GameIntent.OnLeaveConfirmationShow)
                skipItems(1)
                assertTrue(awaitItem().shouldShowLeaveConfirmation)

                // when
                sut.sendIntent(GameIntent.OnLeaveConfirmationDismiss)

                // then
                assertFalse(awaitItem().shouldShowLeaveConfirmation)
            }
        }

    @Test
    fun `when OnLeaveConfirmationConfirm intent is sent then NavigateBack effect is emitted`() =
        runTest {
            // given
            sut.sendIntent(GameIntent.OnLeaveConfirmationShow)
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(GameIntent.OnLeaveConfirmationConfirm)
            testScheduler.advanceUntilIdle()

            // then
            sut.uiEffect.test {
                assertTrue(awaitItem() is GameEffect.NavigateBack)
            }
        }

    @Test
    fun `when OnLeaveConfirmationConfirm intent is sent then shouldShowLeaveConfirmation is false`() =
        runTest {
            // given
            sut.sendIntent(GameIntent.OnLeaveConfirmationShow)
            testScheduler.advanceUntilIdle()
            assertTrue(sut.uiState.value.shouldShowLeaveConfirmation)

            // when
            sut.sendIntent(GameIntent.OnLeaveConfirmationConfirm)
            testScheduler.advanceUntilIdle()

            // then
            assertFalse(sut.uiState.value.shouldShowLeaveConfirmation)
        }

    @Test
    fun `when OnLeaveConfirmationShow is sent twice then shouldShowLeaveConfirmation remains true`() =
        runTest {
            // given
            sut.sendIntent(GameIntent.OnLeaveConfirmationShow)
            testScheduler.advanceUntilIdle()

            // when
            sut.sendIntent(GameIntent.OnLeaveConfirmationShow)
            testScheduler.advanceUntilIdle()

            // then
            assertTrue(sut.uiState.value.shouldShowLeaveConfirmation)
        }

    @Test
    fun `when OnLeaveConfirmationDismiss is sent without prior show then shouldShowLeaveConfirmation remains false`() =
        runTest {
            // when
            sut.sendIntent(GameIntent.OnLeaveConfirmationDismiss)
            testScheduler.advanceUntilIdle()

            // then
            assertFalse(sut.uiState.value.shouldShowLeaveConfirmation)
        }

    @Test
    fun `when OnResetState intent is sent then state is reset to default`() = runTest {
        // given
        sut.sendIntent(GameIntent.OnBoardStart("2x3", false))
        testScheduler.advanceUntilIdle()

        // when
        sut.sendIntent(GameIntent.OnResetState)
        testScheduler.advanceUntilIdle()

        // then
        assertTrue(sut.uiState.value.cards.isEmpty())
        assertFalse(sut.uiState.value.isDailyChallenge)
    }

    @Test
    fun `when OnMistakeShakeComplete intent is sent then shaking cards are flipped back`() =
        runTest {
            // given
            sut.sendIntent(GameIntent.OnBoardStart("2x3", false))
            testScheduler.advanceUntilIdle()
            val cards = sut.uiState.value.cards
            if (cards.size >= 3) {
                sut.sendIntent(GameIntent.OnBackCardClick(cards[0]))
                testScheduler.advanceUntilIdle()
                sut.sendIntent(GameIntent.OnBackCardClick(cards[2]))
                testScheduler.advanceUntilIdle()
            }

            // when
            sut.sendIntent(GameIntent.OnMistakeShakeComplete)
            testScheduler.advanceUntilIdle()

            // then
            val updatedCards = sut.uiState.value.cards
            assertFalse(updatedCards.any { it.isMistakeShaking })
            assertFalse(updatedCards.any { !it.isPairMatched && it.isFlippedFront })
        }

    private fun mockShuffledCardsWithSamePairIds(): List<CardModel> {
        return listOf(
            CardModel.Image("banana_whole", "banana", Res.string.banana, 1),
            CardModel.Image("banana_half", "banana", Res.string.banana, 1),
            CardModel.Image("apple_whole", "apple", Res.string.apple, 2),
            CardModel.Text("apple_half", "apple", Res.string.apple),
            CardModel.Text("strawberry_whole", "strawberry", Res.string.strawberry),
            CardModel.Text("strawberry_half", "strawberry", Res.string.strawberry)
        )
    }

    private fun mockShuffledCardsWithSameIds(): List<CardModel> {
        return listOf(
            CardModel.Image("john_snow", "john_snow", Res.string.empty, 1),
            CardModel.Image("stark", "john_snow", Res.string.empty, 1),
            CardModel.Image("arya_stark", "arya_stark", Res.string.empty, 2),
            CardModel.Image("stark", "arya_stark", Res.string.empty, 1),
            CardModel.Text("sansa_stark", "sansa_stark", Res.string.empty),
            CardModel.Image("stark", "sansa_stark", Res.string.empty, 1),
            CardModel.Text("daenerys_targaryen", "daenerys_targaryen", Res.string.empty),
            CardModel.Image("targaryen", "daenerys_targaryen", Res.string.empty, 1),
        )
    }
}
