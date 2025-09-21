package com.wojdor.memolki.ui.feature.chooselevel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelIntent.OnLevelClick
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChooseLevelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val getLevelsUseCase: GetLevelsUseCase
) : MviViewModel<ChooseLevelIntent, ChooseLevelState>(
    savedStateHandle,
    ChooseLevelState()
) {

    init {
        loadLevels()
    }

    override fun onIntent(intent: ChooseLevelIntent) {
        when (intent) {
            is OnLevelClick -> onLevelClick(intent)
        }
    }

    private fun onLevelClick(intent: OnLevelClick) {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseLevelEffect.OpenGameScreen(levelModel = intent.levelModel))
    }

    private fun loadLevels() {
        getLevelsUseCase().onEach {
            it.onSuccess { levels ->
                sendState { copy(levels = levels) }
            }
        }.launchIn(viewModelScope)
    }
}
