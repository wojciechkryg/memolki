package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCards
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getShuffledUnlockedCards: GetShuffledUnlockedCards
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> onStartGame(intent.levelModel)
            is GameIntent.OnCardClick -> onCardClick(intent.cardModel)
        }
    }

    private fun onStartGame(level: LevelModel) {
        getShuffledUnlockedCards(level).onEach {
            it.onSuccess { cards ->
                sendState { copy(level = level, cards = cards) }
            }
        }.launchIn(viewModelScope)
    }

    private fun onCardClick(card: CardModel) {
        uiState.value.cards.flatten().find { it == card }?.let { foundCard ->
            val cards = uiState.value.cards.map { row ->
                row.map {
                    if (it == foundCard) {
                        when (it) {
                            is CardModel.Text -> it.copy(isFlipped = !it.isFlipped)
                            is CardModel.Image -> it.copy(isFlipped = !it.isFlipped)
                            CardModel.Empty -> it
                        }
                    } else {
                        it
                    }
                }
            }
            sendState { copy(cards = cards) }
        }
    }
}
