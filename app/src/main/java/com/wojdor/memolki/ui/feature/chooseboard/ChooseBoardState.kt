package com.wojdor.memolki.ui.feature.chooseboard

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseBoardState(
    val boards: List<BoardModel> = emptyList(),
    val isDailyChallengeCompleted: Boolean = false,
    val hasDailyChallengeHistory: Boolean = false,
    val hasPlayedAnyGame: Boolean = false
) : UiState
