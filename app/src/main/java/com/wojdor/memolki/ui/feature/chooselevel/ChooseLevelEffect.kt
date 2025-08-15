package com.wojdor.memolki.ui.feature.chooselevel

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class ChooseLevelEffect : UiEffect {
    data class OpenGameScreen(val levelModel: LevelModel) : ChooseLevelEffect()
}
