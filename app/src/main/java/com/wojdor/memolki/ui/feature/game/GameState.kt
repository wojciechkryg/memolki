package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState

data class GameState(
    val levelModel: LevelModel = LevelModel.NoLevel
) : UiState
