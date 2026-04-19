package com.wojdor.memolki.ui.feature.dailychallengehistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.GetAllDailyChallengesUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DailyChallengeHistoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val getAllDailyChallengesUseCase: GetAllDailyChallengesUseCase,
    private val dailyChallengeShareFormatter: DailyChallengeShareFormatter,
    private val timeProvider: TimeProvider
) : MviViewModel<DailyChallengeHistoryIntent, DailyChallengeHistoryState>(
    savedStateHandle,
    DailyChallengeHistoryState.serializer(),
    DailyChallengeHistoryState()
) {

    init {
        loadHistory()
    }

    override fun onIntent(intent: DailyChallengeHistoryIntent) {
        when (intent) {
            is DailyChallengeHistoryIntent.OnShareClick -> onShareClick(intent.dailyChallenge)
        }
    }

    private fun loadHistory() {
        val todayEpochDay = timeProvider.currentLocalDate().toEpochDay()
        sendState { copy(todayEpochDay = todayEpochDay) }
        analytics.logDailyChallengeHistoryOpened()
        getAllDailyChallengesUseCase().onEach { result ->
            result.onSuccess { challenges ->
                sendState { copy(challenges = challenges) }
            }.onFailure {
                logE("Failed to load daily challenge history", it)
            }
        }.launchIn(viewModelScope)
    }

    private fun onShareClick(dailyChallenge: DailyChallengeModel) {
        hapticFeedback.vibrateLow()
        val shareText = dailyChallengeShareFormatter.format(dailyChallenge)
        sendEffect(DailyChallengeHistoryEffect.ShareDailyChallenge(shareText))
        analytics.logDailyChallengeHistoryShareClicked(dailyChallenge.epochDay)
    }
}
