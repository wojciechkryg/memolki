package com.wojdor.memolki.ui.feature.chooselevel

import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class ChooseLevelIntent : UiIntent {
    data class OnLevelClick(val levelModel: LevelModel) : ChooseLevelIntent()
}
