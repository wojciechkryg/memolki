package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel

data class GameCallbacks(
    val onBackCardClick: (CardModel) -> Unit = {},
)
