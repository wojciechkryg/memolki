package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class EndGameIntent : UiIntent {
    data class OnEndGameShow(val levelModel: LevelModel) : EndGameIntent()
}
