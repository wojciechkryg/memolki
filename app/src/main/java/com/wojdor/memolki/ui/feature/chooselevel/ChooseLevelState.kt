package com.wojdor.memolki.ui.feature.chooselevel

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseLevelState(
    val levels: List<LevelModel> = emptyList()
) : UiState
