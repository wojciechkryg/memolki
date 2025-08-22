package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameState(
    val level: LevelModel = LevelModel.Empty,
    val cards: List<List<CardModel>> = emptyList(),
) : UiState
