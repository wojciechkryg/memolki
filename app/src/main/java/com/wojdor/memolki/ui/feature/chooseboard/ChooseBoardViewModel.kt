package com.wojdor.memolki.ui.feature.chooseboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetBoardsUseCase
import com.wojdor.memolki.domain.usecase.HasAnyDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnBoardClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnDailyChallengeHistoryClick
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardIntent.OnLockedBoardClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChooseBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val getBoardsUseCase: GetBoardsUseCase,
    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase,
    private val hasAnyDailyChallengeUseCase: HasAnyDailyChallengeUseCase
) : MviViewModel<ChooseBoardIntent, ChooseBoardState>(
    savedStateHandle,
    ChooseBoardState()
) {

    init {
        loadBoards()
        checkDailyChallengeStatus()
        checkDailyChallengeHistory()
    }

    override fun onIntent(intent: ChooseBoardIntent) {
        when (intent) {
            is OnBoardClick -> onBoardClick(intent)
            is OnDailyChallengeClick -> onDailyChallengeClick()
            is OnLockedBoardClick -> onLockedBoardClick()
            is OnDailyChallengeHistoryClick -> onDailyChallengeHistoryClick()
        }
    }

    private fun onBoardClick(intent: OnBoardClick) {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseBoardEffect.OpenGameScreen(boardModel = intent.boardModel))
    }

    private fun onDailyChallengeClick() {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseBoardEffect.OpenDailyChallengeScreen)
    }

    private fun onLockedBoardClick() {
        hapticFeedback.vibrateLow()
        analytics.logCollectionOpenedFromLockedBoard()
        sendEffect(ChooseBoardEffect.OpenCollectionScreen)
    }

    private fun onDailyChallengeHistoryClick() {
        hapticFeedback.vibrateLow()
        sendEffect(ChooseBoardEffect.OpenDailyChallengeHistoryScreen)
    }

    private fun checkDailyChallengeStatus() {
        hasPlayedTodayDailyChallengeUseCase().onEach { result ->
            result.onSuccess { hasPlayed ->
                sendState { copy(isDailyChallengeCompleted = hasPlayed) }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkDailyChallengeHistory() {
        hasAnyDailyChallengeUseCase().onEach { result ->
            result.onSuccess { hasHistory ->
                sendState { copy(hasDailyChallengeHistory = hasHistory) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadBoards() {
        getBoardsUseCase().onEach {
            it.onSuccess { boards ->
                sendState { copy(boards = boards) }
            }
        }.launchIn(viewModelScope)
    }
}
