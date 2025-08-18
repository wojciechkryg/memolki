package com.wojdor.memolki.ui.feature.game

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.domain.model.LevelModel
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

    fun setLevel(levelModel: LevelModel) {
        sendState { copy(level = levelModel) }
    }

    override fun onIntent(intent: GameIntent) {
        TODO("Not yet implemented")
    }
}
