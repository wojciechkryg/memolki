package com.wojdor.memolki.ui.feature.endgame

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class EndGameEffect : UiEffect {
    data class OpenGameScreen(val levelModel: LevelModel) : EndGameEffect()
    object OpenMenuScreen : EndGameEffect()
}
