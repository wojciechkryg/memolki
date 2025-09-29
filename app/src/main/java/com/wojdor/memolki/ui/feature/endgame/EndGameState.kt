package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class EndGameState(
    val level: LevelModel = LevelModel.Empty,
    val currentCoins: Long = 0L,
    val rewardedCoins: Long = 0L,
    val menu: List<EndGameMenuModel> = emptyList(),
    val animateCoins: Boolean = false,
    val animateRewardCoins: Boolean = false
) : UiState
