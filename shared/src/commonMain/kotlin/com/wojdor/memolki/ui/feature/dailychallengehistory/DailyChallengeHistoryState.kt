package com.wojdor.memolki.ui.feature.dailychallengehistory

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.serialization.Serializable

@Serializable
data class DailyChallengeHistoryState(
    val challenges: List<DailyChallengeModel> = emptyList(),
    val todayEpochDay: Long = 0L
) : UiState
