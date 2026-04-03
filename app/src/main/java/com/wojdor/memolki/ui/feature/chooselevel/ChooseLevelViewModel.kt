package com.wojdor.memolki.ui.feature.chooselevel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.chooselevel.ChooseLevelIntent.OnDailyChallengeClick
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
    private val getLevelsUseCase: GetLevelsUseCase,
    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase
) : MviViewModel<ChooseLevelIntent, ChooseLevelState>(
    savedStateHandle,
    ChooseLevelState()
) {

    init {
        loadLevels()
        checkDailyChallengeStatus()
    }

    override fun onIntent(intent: ChooseLevelIntent) {
        when (intent) {
            is OnLevelClick -> onLevelClick(intent)
            is OnDailyChallengeClick -> onDailyChallengeClick()
        }
    }

    private fun onLevelClick(intent: OnLevelClick) {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseLevelEffect.OpenGameScreen(levelModel = intent.levelModel))
    }

    private fun onDailyChallengeClick() {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseLevelEffect.OpenDailyChallengeScreen)
    }

    private fun checkDailyChallengeStatus() {
        hasPlayedTodayDailyChallengeUseCase().onEach { result ->
            result.onSuccess { hasPlayed ->
                sendState { copy(isDailyChallengeCompleted = hasPlayed) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadLevels() {
        getLevelsUseCase().onEach {
            it.onSuccess { levels ->
                sendState { copy(levels = levels) }
            }
        }.launchIn(viewModelScope)
    }
}
