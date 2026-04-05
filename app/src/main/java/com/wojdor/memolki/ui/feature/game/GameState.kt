package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameState(
    val level: LevelModel = LevelModel.Empty,
    val cards: List<CardModel> = emptyList(),
    val isGameFinished: Boolean = false,
    val lastCardPressed: CardModel = CardModel.Empty,
    val shouldShowCardText: Boolean = false,
    val shouldShowCardDetails: Boolean = false,
    val mistakeCount: Int = 0,
    val cardFlipCounts: List<List<Int>> = emptyList(),
    val isDailyChallenge: Boolean = false,
    val epochDay: Long = 0L,
    val startTimeMillis: Long = 0L,
    val levelPlayedCount: Long = 1L
) : UiState
