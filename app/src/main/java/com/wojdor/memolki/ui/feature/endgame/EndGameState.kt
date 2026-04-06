package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class EndGameState(
    val board: BoardModel = BoardModel.Empty,
    val currentCoins: Long = 0L,
    val rewardedCoins: Long = 0L,
    val menu: List<EndGameMenuModel> = emptyList(),
    val animateCoins: Boolean = false,
    val animateRewardCoins: Boolean = false,
    val showSparkles: Boolean = false,
    val dailyChallenge: DailyChallengeModel = DailyChallengeModel(),
    val isDailyChallenge: Boolean = false,
    val level: Long = BoardModel.DEFAULT_LEVEL
) : UiState
