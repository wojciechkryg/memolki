package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : MviViewModel<GameIntent, GameState>(
    savedStateHandle,
    GameState()
) {

    override fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.OnLevelStart -> sendState { copy(level = intent.levelModel) }
        }
    }
}
