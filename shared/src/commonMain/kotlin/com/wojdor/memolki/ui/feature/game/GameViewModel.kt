package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.StarCalculator
import com.wojdor.memolki.domain.usecase.GetBiggestUnlockedBoardUseCase
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetLevelUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.IncrementLevelUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val cardFlipPlayer: CardFlipPlayer,
    private val cardPairMatchedPlayer: CardPairMatchedPlayer,
    private val hapticFeedback: HapticFeedback,
    private val getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase,
    private val incrementTotalCardPairsMatchedUseCase: IncrementTotalCardPairsMatchedUseCase,
    private val getLevelUseCase: GetLevelUseCase,
    private val incrementLevelUseCase: IncrementLevelUseCase,
    private val getBiggestUnlockedBoardUseCase: GetBiggestUnlockedBoardUseCase,
    private val getDailyChallengeCardsUseCase: GetDailyChallengeCardsUseCase,
    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase,
    private val saveDailyChallengeUseCase: SaveDailyChallengeUseCase,
    private val getTodayDailyChallengeUseCase: GetTodayDailyChallengeUseCase,
    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase,
    private val timeProvider: TimeProvider,
    private val starCalculator: StarCalculator
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState.serializer(),
    GameState()
) {

    private var levelFlowJob: Job? = null

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnBoardStart -> onBoardStart(intent.boardId, intent.isDailyChallenge)
            is GameIntent.OnBackCardClick -> onBackCardClick(intent.cardModel)
            is GameIntent.OnFrontCardPress -> onFrontCardPress(intent.isPressed, intent.cardModel)
            GameIntent.OnMatchAnimationComplete -> onMatchAnimationComplete()
            GameIntent.OnMistakeShakeComplete -> onMistakeShakeComplete()
            GameIntent.OnGameLeave -> onGameLeave()
            GameIntent.OnLeaveConfirmationShow -> onLeaveConfirmationShow()
            GameIntent.OnLeaveConfirmationDismiss -> onLeaveConfirmationDismiss()
            GameIntent.OnLeaveConfirmationConfirm -> onLeaveConfirmationConfirm()
            GameIntent.OnResetState -> sendState { GameState() }
        }
    }

    private fun onBoardStart(boardId: String, isDailyChallenge: Boolean) {
        if (isDailyChallenge) {
            onDailyChallengeStart()
        } else {
            getAndStartBoard(boardId)
        }
    }

    private fun getAndStartBoard(boardId: String) {
        getBiggestUnlockedBoardUseCase(boardId).onEach { result ->
            result.onSuccess { board -> shuffleUnlockedCards(board) }
        }.launchIn(viewModelScope)
    }

    private fun shuffleUnlockedCards(board: BoardModel) {
        analytics.logBoardStart(board.columns, board.rows)
        getShuffledUnlockedCardsUseCase(board).onEach {
            it.onSuccess { cards ->
                sendState {
                    copy(
                        board = board,
                        cards = cards,
                        cardFlipCounts = emptyFlipCountsGrid(cards, board.columns)
                    )
                }
            }
        }.launchIn(viewModelScope)
        levelFlowJob?.cancel()
        levelFlowJob = getLevelUseCase(board.id).onEach { result ->
            result.onSuccess { count ->
                if (!uiState.value.isGameFinished) {
                    sendState { copy(level = count) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onDailyChallengeStart() {
        if (uiState.value.cards.isNotEmpty()) return
        val epochDay = timeProvider.currentLocalDate().toEpochDays()
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
                        boardModel = BoardModel.DAILY_CHALLENGE,
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
        getDailyChallengeCardsUseCase(BoardModel.DAILY_CHALLENGE).onEach { result ->
            result.onSuccess { cards ->
                sendState {
                    copy(
                        board = BoardModel.DAILY_CHALLENGE,
                        cards = cards,
                        cardFlipCounts = emptyFlipCountsGrid(
                            cards,
                            BoardModel.DAILY_CHALLENGE.columns
                        ),
                        isDailyChallenge = true,
                        epochDay = epochDay
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveDailyChallengeAndOpenEndScreen() {
        val state = uiState.value
        val elapsedTimeMillis = timeProvider.currentTimeMillis() - state.startTimeMillis
        val starCount = starCalculator.calculate(state.mistakeCount)
        val result = DailyChallengeModel(
            mistakeCount = state.mistakeCount,
            starCount = starCount,
            timeMillis = elapsedTimeMillis,
            epochDay = state.epochDay,
            cardFlipCounts = state.cardFlipCounts
        )
        viewModelScope.launch {
            val totalGames = getTotalGamesPlayedUseCase().first().getOrNull()
            analytics.logDailyChallengeComplete(
                epochDay = state.epochDay,
                mistakeCount = state.mistakeCount,
                starCount = starCount,
                timeMillis = elapsedTimeMillis,
                totalGamesCount = totalGames?.plus(1)
            )
        }
        saveDailyChallengeUseCase(result).launchIn(viewModelScope)
        sendEffect(
            GameEffect.OpenEndGameScreen(
                boardModel = BoardModel.DAILY_CHALLENGE,
                mistakeCount = state.mistakeCount,
                cardFlipCounts = state.cardFlipCounts,
                dailyChallenge = result
            )
        )
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
        if (uiState.value.cards.none { it.isMatchAnimating }) return
        mapCards { card ->
            if (card.isMatchAnimating) card.copyState(isMatchAnimating = false) else card
        }
        sendEffect(GameEffect.OnPairMatched)
        if (uiState.value.isGameFinished) {
            navigateToEndGame()
        }
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
        val cards = uiState.value.cards
        if (cards.isNotEmpty() && cards.all { it.isPairMatched }) {
            if (!uiState.value.isDailyChallenge) {
                val state = uiState.value
                viewModelScope.launch {
                    val totalGames = getTotalGamesPlayedUseCase().first().getOrNull()
                    analytics.logBoardComplete(
                        columns = state.board.columns,
                        rows = state.board.rows,
                        mistakeCount = state.mistakeCount,
                        level = state.level,
                        totalGamesCount = totalGames?.plus(1)
                    )
                }
                incrementLevelUseCase(uiState.value.board.id).launchIn(viewModelScope)
            }
            sendState { copy(isGameFinished = true) }
        }
    }

    private fun navigateToEndGame() {
        if (uiState.value.isDailyChallenge) {
            saveDailyChallengeAndOpenEndScreen()
        } else {
            sendEffect(
                GameEffect.OpenEndGameScreen(
                    boardModel = uiState.value.board,
                    mistakeCount = uiState.value.mistakeCount,
                    cardFlipCounts = uiState.value.cardFlipCounts,
                    level = uiState.value.level
                )
            )
        }
        levelFlowJob?.cancel()
        levelFlowJob = null
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
                sendEffect(GameEffect.SendTotalCardPairsMatchedScore(it))
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
        val columns = uiState.value.board.columns
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
                startTimeMillis = if (uiState.value.isDailyChallenge && startTimeMillis == 0L) timeProvider.currentTimeMillis() else startTimeMillis
            )
        }
    }

    private fun updateCard(old: CardModel, new: CardModel): List<CardModel> =
        uiState.value.cards.map { if (it.pairId == old.pairId && it.id == old.id) new else it }

    private fun incrementFlipCount(index: Int): List<List<Int>> {
        if (index < 0) return uiState.value.cardFlipCounts
        val columns = uiState.value.board.columns
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
        sendState { copy(cards = updatedCards, progress = calculateProgress(updatedCards)) }
    }

    private fun calculateProgress(cards: List<CardModel>): Float =
        cards.count { it.isPairMatched }.toFloat() / cards.size.coerceAtLeast(1)

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
            if (uiState.value.isDailyChallenge) {
                analytics.logDailyChallengeAbandoned(state.epochDay)
            } else {
                analytics.logBoardAbandoned(state.board.columns, state.board.rows)
            }
        }
    }

    private fun onLeaveConfirmationShow() {
        sendState { copy(shouldShowLeaveConfirmation = true) }
    }

    private fun onLeaveConfirmationDismiss() {
        sendState { copy(shouldShowLeaveConfirmation = false) }
    }

    private fun onLeaveConfirmationConfirm() {
        sendState { copy(shouldShowLeaveConfirmation = false) }
        sendEffect(GameEffect.NavigateBack)
    }

    private fun emptyFlipCountsGrid(cards: List<CardModel>, columns: Int): List<List<Int>> =
        cards.chunked(columns.coerceAtLeast(1)).map { row -> List(row.size) { 0 } }

    companion object {
        const val MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS = 2
    }
}
