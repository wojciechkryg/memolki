package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class EndGameState(
    val level: LevelModel = LevelModel.Empty,
    val rewardedCoins: Long = 0,
    val menu: List<EndGameMenuModel> = emptyList()
) : UiState
