package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.LevelModel

data class CollectionCallbacks(
    val onLevelClick: (levelModel: LevelModel) -> Unit = {}
)
