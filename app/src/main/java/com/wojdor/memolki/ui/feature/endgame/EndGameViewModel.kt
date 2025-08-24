package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : MviViewModel<EndGameIntent, EndGameState>(
    savedStateHandle,
    EndGameState()
) {

    override fun onIntent(intent: EndGameIntent) {

    }
}
