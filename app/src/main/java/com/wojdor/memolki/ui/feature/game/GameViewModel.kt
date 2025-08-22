package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCards
import com.wojdor.memolki.ui.base.MviViewModel
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
    private val getShuffledUnlockedCards: GetShuffledUnlockedCards
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    private var flipToBackJob: Job? = null

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> shuffleUnlockedCards(intent.levelModel)
            is GameIntent.OnBackCardClick -> onBackCardClick(intent.cardModel)
        }
    }

    private fun shuffleUnlockedCards(level: LevelModel) {
        getShuffledUnlockedCards(level).onEach {
            it.onSuccess { cards ->
                sendState { copy(level = level, cards = cards) }
            }
        }.launchIn(viewModelScope)
    }

    private fun onBackCardClick(card: CardModel) {
        if (isTooManyFlippedToFrontUnmatchedCards()) {
            immediatelyFlipToBackUnmatchedCards()
        }
        val cardFlipped = flipCardToFront(card)
        updateStateWith(cardFlipped)
        if (isTooManyFlippedToFrontUnmatchedCards()) {
            flipToBackUnmatchedCardsWithDelay()
        }
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
        return uiState.value.cards.flatten().filter {
            it.isFlippedFront && !it.isMatched
        }
    }

    private fun flipToBackUnmatchedCards() {
        val changedCards = mutableListOf<CardModel>()
        frontUnmatchedCards().forEach {
            changedCards.add(flipCardToBack(it))
        }
        updateStateWith(changedCards)
    }

    private fun flipCardToFront(card: CardModel): CardModel {
        return flipCard(card, true)
    }

    private fun flipCardToBack(card: CardModel): CardModel {
        return flipCard(card, false)
    }

    private fun flipCard(card: CardModel, isFlippedFront: Boolean): CardModel {
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
        val cardsToUpdateMap = cardsToUpdate.associateBy { it.id }
        val updatedCards = uiState.value.cards.map { column ->
            column.map { card ->
                cardsToUpdateMap[card.id] ?: card
            }
        }
        sendState { copy(cards = updatedCards) }
    }

    companion object {
        const val MAX_FLIPPED_TO_FRONT_UNMATCHED_CARDS = 2
        const val FLIP_TO_BACK_DELAY = 2000L
    }
}
