package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.ResolveLevelUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.playgames.GooglePlayGames
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
    private val resolveLevelUseCase: ResolveLevelUseCase
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> resolveAndStartLevel(intent.levelId)
            is GameIntent.OnBackCardClick -> onBackCardClick(intent.cardModel)
            is GameIntent.OnFrontCardPress -> onFrontCardPress(intent.isPressed, intent.cardModel)
            GameIntent.OnMatchAnimationComplete -> onMatchAnimationComplete()
            GameIntent.OnMismatchShakeComplete -> onMismatchShakeComplete()
            GameIntent.OnGameLeave -> onGameLeave()
        }
    }

    private fun resolveAndStartLevel(levelId: String) {
        resolveLevelUseCase(levelId).onEach { result ->
            result.onSuccess { level -> shuffleUnlockedCards(level) }
        }.launchIn(viewModelScope)
    }

    private fun shuffleUnlockedCards(level: LevelModel) {
        sendState { copy(mismatchCount = 0) }
        analytics.logLevelStart(level)
        getShuffledUnlockedCardsUseCase(level).onEach {
            it.onSuccess { cards ->
                sendState { copy(level = level, cards = cards) }
            }
        }.launchIn(viewModelScope)
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
            startMismatchShake()
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
    }

    private fun onMismatchShakeComplete() {
        mapCards { card ->
            if (card.isMismatchShaking) {
                card.copyState(isMismatchShaking = false, isFlippedFront = false)
            } else {
                card
            }
        }
    }

    private fun checkForEndGame() {
        viewModelScope.launch {
            val cards = uiState.value.cards
            if (cards.isNotEmpty() && cards.all { it.isPairMatched }) {
                analytics.logLevelComplete(uiState.value.level, uiState.value.mismatchCount)
                sendState { copy(isGameFinished = true) }
                delay(END_GAME_DELAY)
                sendEffect(GameEffect.OpenEndGameScreen(uiState.value.level))
                sendState {
                    copy(
                        isGameFinished = false,
                        shouldShowCardText = false,
                        shouldShowCardDetails = false,
                        lastCardPressed = CardModel.Empty,
                        mismatchCount = 0
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
                delay(250L)
                cardPairMatchedPlayer.play()
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

    private fun startMismatchShake() {
        sendState { copy(mismatchCount = mismatchCount + 1) }
        val unmatchedCards = frontUnmatchedCards().map { card ->
            card.copyState(isMismatchShaking = true)
        }
        updateStateWith(unmatchedCards)
    }

    private fun frontUnmatchedCards(): List<CardModel> {
        return uiState.value.cards.filter {
            it.isFlippedFront && !it.isPairMatched
        }
    }

    private fun flipToBackUnmatchedCards() {
        val changedCards = frontUnmatchedCards().map { card ->
            card.copyState(isFlippedFront = false, isMismatchShaking = false)
        }
        updateStateWith(changedCards)
    }

    private fun flipCardToFront(card: CardModel) {
        cardFlipPlayer.play()
        updateStateWith(card.copyState(isFlippedFront = true, isMismatchShaking = false))
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
            analytics.logLevelAbandon(state.level)
        }
    }

    companion object {
        const val MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS = 2
        const val END_GAME_DELAY = 1000L
    }
}
