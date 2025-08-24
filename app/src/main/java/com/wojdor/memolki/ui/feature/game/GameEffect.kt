package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class GameEffect : UiEffect {
    data class OpenEndGameScreen(val levelModel: LevelModel) : GameEffect()
}
