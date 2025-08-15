package com.wojdor.memolki.ui.feature.chooselevel

import com.wojdor.memolki.domain.model.LevelModel

data class ChooseLevelCallbacks(
    val onLevelClick: (levelModel: LevelModel) -> Unit = {}
)
