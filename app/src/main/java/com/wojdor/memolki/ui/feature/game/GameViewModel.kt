package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cardFlipPlayer: CardFlipPlayer,
    private val cardPairMatchedPlayer: CardPairMatchedPlayer,
    private val hapticFeedback: HapticFeedback,
    private val googlePlayGames: GooglePlayGames,
    private val getShuffledUnlockedCardsUseCase: GetShuffledUnlockedCardsUseCase,
    private val incrementTotalCardPairsMatchedUseCase: IncrementTotalCardPairsMatchedUseCase
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    private var flipToBackJob: Job? = null

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> shuffleUnlockedCards(intent.levelModel)
            is GameIntent.OnBackCardClick -> onBackCardClick(intent.cardModel)
            is GameIntent.OnFrontCardPress -> onFrontCardPress(intent.isPressed, intent.cardModel)
        }
    }

    private fun shuffleUnlockedCards(level: LevelModel) {
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
            immediatelyFlipToBackUnmatchedCards()
        }
        flipCardToFront(card)
        checkForMatchedPair()
        checkForEndGame()
        if (isTooManyFlippedToFrontUnmatchedCards()) {
            flipToBackUnmatchedCardsWithDelay()
        }
    }

    private fun onFrontCardPress(
        isPressed: Boolean,
        card: CardModel
    ) {
        if (isPressed && card is CardModel.Image && card.isPairMatched) {
            hapticFeedback.vibrateLow()
            showCardText(card)
        } else {
            hideCardText()
        }
    }

    private fun checkForEndGame() {
        viewModelScope.launch {
            val cards = uiState.value.cards
            if (cards.isNotEmpty() && cards.all { it.isPairMatched }) {
                sendState { copy(isGameFinished = true) }
                delay(END_GAME_DELAY)
                sendEffect(GameEffect.OpenEndGameScreen(uiState.value.level))
                sendState {
                    copy(
                        isGameFinished = false,
                        shouldShowCardText = false,
                        lastCardPressed = CardModel.Empty
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
        val matchedCards = frontUnmatchedCards.map { cardToMatch ->
            when (cardToMatch) {
                is CardModel.Text -> cardToMatch.copy(isPairMatched = true)
                is CardModel.Image -> cardToMatch.copy(isPairMatched = true)
                is CardModel.Empty -> cardToMatch
            }
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

    private fun flipToBackUnmatchedCardsWithDelay() {
        flipToBackJob = viewModelScope.launch {
            delay(FLIP_TO_BACK_DELAY)
            flipToBackUnmatchedCards()
        }
    }

    private fun immediatelyFlipToBackUnmatchedCards() {
        flipToBackJob?.cancel()
        flipToBackJob = null
        flipToBackUnmatchedCards()
    }

    private fun frontUnmatchedCards(): List<CardModel> {
        return uiState.value.cards.filter {
            it.isFlippedFront && !it.isPairMatched
        }
    }

    private fun flipToBackUnmatchedCards() {
        val changedCards = frontUnmatchedCards().map {
            markCardAsFlippedToBack(it)
        }
        updateStateWith(changedCards)
    }

    private fun flipCardToFront(card: CardModel) {
        cardFlipPlayer.play()
        updateStateWith(markCardAsFlipped(card, true))
    }

    private fun markCardAsFlippedToBack(card: CardModel): CardModel {
        return markCardAsFlipped(card, false)
    }

    private fun markCardAsFlipped(card: CardModel, isFlippedFront: Boolean): CardModel {
        return when (card) {
            is CardModel.Text -> card.copy(isFlippedFront = isFlippedFront)
            is CardModel.Image -> card.copy(isFlippedFront = isFlippedFront)
            CardModel.Empty -> card
        }
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

    companion object {
        const val MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS = 2
        const val FLIP_TO_BACK_DELAY = 2000L
        const val END_GAME_DELAY = 1000L
    }
}
