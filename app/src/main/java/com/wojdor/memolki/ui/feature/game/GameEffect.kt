package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.util.playgames.GooglePlayGames

sealed class GameEffect : UiEffect {
    data class OpenEndGameScreen(
        val levelModel: LevelModel,
        val mistakeCount: Int,
        val cardFlipCounts: List<List<Int>>,
        val dailyChallenge: DailyChallengeModel = DailyChallengeModel()
    ) : GameEffect()

    data class SendTotalCardPairsMatchedScore(
        val googlePlayGames: GooglePlayGames,
        val totalCardPairsMatched: Long
    ) : GameEffect()

    data object PlayMatchSound : GameEffect()
}
