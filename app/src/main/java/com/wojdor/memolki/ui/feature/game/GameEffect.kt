package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.util.playgames.GooglePlayGames

sealed class GameEffect : UiEffect {
    data class OpenEndGameScreen(
        val boardModel: BoardModel,
        val mistakeCount: Int,
        val cardFlipCounts: List<List<Int>>,
        val level: Long = BoardModel.DEFAULT_LEVEL,
        val dailyChallenge: DailyChallengeModel = DailyChallengeModel()
    ) : GameEffect()

    data class SendTotalCardPairsMatchedScore(
        val googlePlayGames: GooglePlayGames,
        val totalCardPairsMatched: Long
    ) : GameEffect()

    data object OnPairMatched : GameEffect()
    data object NavigateBack : GameEffect()
}
