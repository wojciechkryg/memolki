package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetLevelPlayedCountUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.IncrementLevelPlayedCountUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.ResolveLevelUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.TimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val cardFlipPlayer: CardFlipPlayer,
    private val cardPairMatchedPlayer: CardPairMatchedPlayer,
    private val hapticFeedback: HapticFeedback,
    private val googlePlayGames: GooglePlayGames,
    private val getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase,
    private val incrementTotalCardPairsMatchedUseCase: IncrementTotalCardPairsMatchedUseCase,
    private val getLevelPlayedCountUseCase: GetLevelPlayedCountUseCase,
    private val incrementLevelPlayedCountUseCase: IncrementLevelPlayedCountUseCase,
    private val resolveLevelUseCase: ResolveLevelUseCase,
    private val getDailyChallengeCardsUseCase: GetDailyChallengeCardsUseCase,
    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase,
    private val saveDailyChallengeUseCase: SaveDailyChallengeUseCase,
    private val getTodayDailyChallengeUseCase: GetTodayDailyChallengeUseCase,
    private val timeProvider: TimeProvider
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> onLevelStart(intent.levelId, intent.isDailyChallenge)
            is GameIntent.OnBackCardClick -> onBackCardClick(intent.cardModel)
            is GameIntent.OnFrontCardPress -> onFrontCardPress(intent.isPressed, intent.cardModel)
            GameIntent.OnMatchAnimationComplete -> onMatchAnimationComplete()
            GameIntent.OnMistakeShakeComplete -> onMistakeShakeComplete()
            GameIntent.OnGameLeave -> onGameLeave()
        }
    }

    private fun onLevelStart(levelId: String, isDailyChallenge: Boolean) {
        if (isDailyChallenge) {
            onDailyChallengeStart()
        } else {
            resolveAndStartLevel(levelId)
        }
    }

    private fun isDailyChallenge() = uiState.value.epochDay > 0L

    private fun resolveAndStartLevel(levelId: String) {
        resolveLevelUseCase(levelId).onEach { result ->
            result.onSuccess { level -> shuffleUnlockedCards(level) }
        }.launchIn(viewModelScope)
    }

    private fun shuffleUnlockedCards(level: LevelModel) {
        analytics.logLevelStart(level)
        getShuffledUnlockedCardsUseCase(level).onEach {
            it.onSuccess { cards ->
                sendState {
                    copy(
                        level = level,
                        cards = cards,
                        cardFlipCounts = emptyFlipCountsGrid(cards, level.columns)
                    )
                }
            }
        }.launchIn(viewModelScope)
        getLevelPlayedCountUseCase(level.id).onEach { result ->
            result.onSuccess { count ->
                sendState { copy(levelPlayedCount = count) }
            }
        }.launchIn(viewModelScope)
    }

    private fun onDailyChallengeStart() {
        if (uiState.value.cards.isNotEmpty()) return
        val epochDay = timeProvider.currentLocalDate().toEpochDay()
        hasPlayedTodayDailyChallengeUseCase().onEach { result ->
            result.onSuccess { hasPlayed ->
                if (hasPlayed) {
                    openAlreadyPlayedDailyChallenge(epochDay)
                } else {
                    startNewDailyChallenge(epochDay)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun openAlreadyPlayedDailyChallenge(epochDay: Long) {
        analytics.logDailyChallengeAlreadyPlayed(epochDay)
        getTodayDailyChallengeUseCase().onEach { result ->
            result.onSuccess { challenge ->
                sendEffect(
                    GameEffect.OpenEndGameScreen(
                        levelModel = DAILY_CHALLENGE_LEVEL,
                        mistakeCount = challenge.mistakeCount,
                        cardFlipCounts = challenge.cardFlipCounts,
                        dailyChallenge = challenge.copy(epochDay = epochDay)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun startNewDailyChallenge(epochDay: Long) {
        saveDailyChallengeUseCase(DailyChallengeModel()).onEach { result ->
            result.onSuccess {
                analytics.logDailyChallengeStart(epochDay)
                loadDailyChallengeCards(epochDay)
            }
        }.launchIn(viewModelScope)
    }

    private fun loadDailyChallengeCards(epochDay: Long) {
        getDailyChallengeCardsUseCase(DAILY_CHALLENGE_LEVEL).onEach { result ->
            result.onSuccess { cards ->
                sendState {
                    copy(
                        level = DAILY_CHALLENGE_LEVEL,
                        cards = cards,
                        cardFlipCounts = emptyFlipCountsGrid(cards, DAILY_CHALLENGE_LEVEL.columns),
                        epochDay = epochDay
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveDailyChallengeAndOpenEndScreen() {
        val state = uiState.value
        val elapsedTimeMillis = timeProvider.currentTimeMillis() - state.startTimeMillis
        val starCount = calculateStars(state.mistakeCount)
        val result = DailyChallengeModel(
            mistakeCount = state.mistakeCount,
            starCount = starCount,
            timeMillis = elapsedTimeMillis,
            epochDay = state.epochDay,
            cardFlipCounts = state.cardFlipCounts
        )
        analytics.logDailyChallengeComplete(
            state.epochDay,
            state.mistakeCount,
            starCount,
            elapsedTimeMillis
        )
        saveDailyChallengeUseCase(result).launchIn(viewModelScope)
        sendEffect(
            GameEffect.OpenEndGameScreen(
                levelModel = DAILY_CHALLENGE_LEVEL,
                mistakeCount = state.mistakeCount,
                cardFlipCounts = state.cardFlipCounts,
                dailyChallenge = result
            )
        )
    }

    private fun calculateStars(mistakeCount: Int): Int = when {
        mistakeCount == 0 -> MAX_STARS
        mistakeCount in 1..4 -> TWO_STARS
        else -> MIN_STARS
    }

    private fun onBackCardClick(card: CardModel) {
        hapticFeedback.vibrateStrong()
        hideCardText()
        if (isTooManyFlippedToFrontUnmatchedCards()) {
            flipToBackUnmatchedCards()
        }
        flipCardToFront(card)
        checkForMatchedPair()
        checkForEndGame()
        if (isTooManyFlippedToFrontUnmatchedCards()) {
            startMistakeShake()
        }
    }

    private fun onFrontCardPress(isPressed: Boolean, card: CardModel) {
        if (isPressed && card is CardModel.Image && card.isPairMatched) {
            hapticFeedback.vibrateLow()
            hideCardText()
            showCardDetails(card)
        } else {
            hideCardDetails()
        }
    }

    private fun onMatchAnimationComplete() {
        mapCards { card ->
            if (card.isMatchAnimating) {
                card.copyState(isMatchAnimating = false)
            } else {
                card
            }
        }
        sendEffect(GameEffect.OnPairMatched)
    }

    fun playMatchSound() {
        cardPairMatchedPlayer.play()
    }

    private fun onMistakeShakeComplete() {
        mapCards { card ->
            if (card.isMistakeShaking) {
                card.copyState(isMistakeShaking = false, isFlippedFront = false)
            } else {
                card
            }
        }
    }

    private fun checkForEndGame() {
        viewModelScope.launch {
            val cards = uiState.value.cards
            if (cards.isNotEmpty() && cards.all { it.isPairMatched }) {
                if (!isDailyChallenge()) {
                    analytics.logLevelComplete(uiState.value.level, uiState.value.mistakeCount)
                    incrementLevelPlayedCountUseCase(uiState.value.level.id).launchIn(viewModelScope)
                }
                sendState { copy(isGameFinished = true) }
                delay(END_GAME_DELAY)
                if (isDailyChallenge()) {
                    saveDailyChallengeAndOpenEndScreen()
                } else {
                    sendEffect(
                        GameEffect.OpenEndGameScreen(
                            levelModel = uiState.value.level,
                            mistakeCount = uiState.value.mistakeCount,
                            cardFlipCounts = uiState.value.cardFlipCounts
                        )
                    )
                }
                sendState {
                    copy(
                        isGameFinished = false,
                        shouldShowCardText = false,
                        shouldShowCardDetails = false,
                        lastCardPressed = CardModel.Empty,
                        mistakeCount = 0,
                        cardFlipCounts = emptyList(),
                        cards = emptyList(),
                        epochDay = 0L,
                        startTimeMillis = 0L,
                        levelPlayedCount = 0
                    )
                }
            }
        }
    }

    private fun checkForMatchedPair() {
        val frontUnmatchedCards = frontUnmatchedCards()
        if (frontUnmatchedCards.size >= MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS) {
            val firstCard = frontUnmatchedCards[0]
            val secondCard = frontUnmatchedCards[1]
            if (areCardsMatched(firstCard, secondCard)) {
                matchCards(frontUnmatchedCards)
            }
        }
    }

    private fun matchCards(frontUnmatchedCards: List<CardModel>) {
        val matchedCards = frontUnmatchedCards.map { card ->
            card.copyState(isPairMatched = true, isMatchAnimating = true)
        }
        updateStateWith(matchedCards)
        showCardText(matchedCards)
        incrementTotalCardPairsMatchedUseCase().onEach { result ->
            result.onSuccess {
                sendEffect(GameEffect.SendTotalCardPairsMatchedScore(googlePlayGames, it))
            }
        }.launchIn(viewModelScope)
    }

    private fun areCardsMatched(
        firstCard: CardModel,
        secondCard: CardModel
    ): Boolean = areFromTheSamePair(firstCard, secondCard) ||
            existAnotherCardWithTheSameIdAsFromMatchingPairId(firstCard, secondCard)

    private fun areFromTheSamePair(
        firstCard: CardModel,
        secondCard: CardModel
    ) = firstCard.pairId == secondCard.pairId

    private fun existAnotherCardWithTheSameIdAsFromMatchingPairId(
        firstCard: CardModel,
        secondCard: CardModel
    ): Boolean {
        val cards = uiState.value.cards
        val matchingFirstCard = cards.find { it != firstCard && it.pairId == firstCard.pairId }
        val matchingSecondCard = cards.find { it != secondCard && it.pairId == secondCard.pairId }
        return firstCard.id == matchingFirstCard?.id
                || secondCard.id == matchingSecondCard?.id
                || firstCard.id == matchingSecondCard?.id
                || secondCard.id == matchingFirstCard?.id
    }

    private fun isTooManyFlippedToFrontUnmatchedCards() =
        frontUnmatchedCards().size >= MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS

    private fun startMistakeShake() {
        val mismatches = frontUnmatchedCards().count { card ->
            getFlipCount(card) >= 2
        }
        if (mismatches > 0) {
            sendState { copy(mistakeCount = mistakeCount + mismatches) }
        }
        val unmatchedCards = frontUnmatchedCards().map { card ->
            card.copyState(isMistakeShaking = true)
        }
        updateStateWith(unmatchedCards)
    }

    private fun frontUnmatchedCards(): List<CardModel> {
        return uiState.value.cards.filter {
            it.isFlippedFront && !it.isPairMatched
        }
    }

    private fun findCardIndex(card: CardModel): Int =
        uiState.value.cards.indexOfFirst { it.pairId == card.pairId && it.id == card.id }

    private fun getFlipCount(card: CardModel): Int {
        val index = findCardIndex(card)
        if (index < 0) return 0
        val columns = uiState.value.level.columns
        return uiState.value.cardFlipCounts[index / columns][index % columns]
    }

    private fun flipToBackUnmatchedCards() {
        val changedCards = frontUnmatchedCards().map { card ->
            card.copyState(isFlippedFront = false, isMistakeShaking = false)
        }
        updateStateWith(changedCards)
    }

    private fun flipCardToFront(card: CardModel) {
        cardFlipPlayer.play()
        val updatedCards =
            updateCard(card, card.copyState(isFlippedFront = true, isMistakeShaking = false))
        val updatedFlipCounts = incrementFlipCount(findCardIndex(card))
        sendState {
            copy(
                cards = updatedCards,
                cardFlipCounts = updatedFlipCounts,
                startTimeMillis = if (isDailyChallenge() && startTimeMillis == 0L) timeProvider.currentTimeMillis() else startTimeMillis
            )
        }
    }

    private fun updateCard(old: CardModel, new: CardModel): List<CardModel> =
        uiState.value.cards.map { if (it.pairId == old.pairId && it.id == old.id) new else it }

    private fun incrementFlipCount(index: Int): List<List<Int>> {
        if (index < 0) return uiState.value.cardFlipCounts
        val columns = uiState.value.level.columns
        val targetRow = index / columns
        val targetColumn = index % columns
        return uiState.value.cardFlipCounts.mapIndexed { rowIndex, rowList ->
            if (rowIndex == targetRow) {
                rowList.mapIndexed { columnIndex, count ->
                    if (columnIndex == targetColumn) count + 1 else count
                }
            } else {
                rowList
            }
        }
    }

    private fun mapCards(transform: (CardModel) -> CardModel) {
        val updatedCards = uiState.value.cards.map(transform)
        sendState { copy(cards = updatedCards) }
    }

    private fun updateStateWith(card: CardModel) {
        updateStateWith(listOf(card))
    }

    private fun updateStateWith(cardsToUpdate: List<CardModel>) {
        val updatedCards = uiState.value.cards.map { card ->
            cardsToUpdate.find { it.pairId == card.pairId && it.id == card.id } ?: card
        }
        sendState { copy(cards = updatedCards) }
    }

    private fun showCardDetails(card: CardModel) {
        sendState {
            copy(
                lastCardPressed = card,
                shouldShowCardDetails = true
            )
        }
    }

    private fun hideCardDetails() {
        sendState { copy(shouldShowCardDetails = false) }
    }

    private fun showCardText(card: CardModel) {
        sendState {
            copy(
                lastCardPressed = card,
                shouldShowCardText = true
            )
        }
    }

    private fun showCardText(matchedCards: List<CardModel>) {
        if (matchedCards.all { it is CardModel.Image }) {
            showCardText(matchedCards.first())
        }
    }

    private fun hideCardText() {
        sendState { copy(shouldShowCardText = false) }
    }

    private fun onGameLeave() {
        val state = uiState.value
        val isGameInProgress = state.cards.isNotEmpty()
                && !state.isGameFinished
                && !state.cards.all { it.isPairMatched }
        if (isGameInProgress) {
            if (isDailyChallenge()) {
                analytics.logDailyChallengeAbandoned(state.epochDay)
            } else {
                analytics.logLevelAbandoned(state.level)
            }
        }
    }

    private fun emptyFlipCountsGrid(cards: List<CardModel>, columns: Int): List<List<Int>> =
        cards.chunked(columns.coerceAtLeast(1)).map { row -> List(row.size) { 0 } }

    companion object {
        val DAILY_CHALLENGE_LEVEL = LevelModel.Grid5x6(isUnlocked = true)
        const val MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS = 2
        const val END_GAME_DELAY = 1000L

        const val MAX_STARS = 3
        const val TWO_STARS = 2
        const val MIN_STARS = 1
    }
}
