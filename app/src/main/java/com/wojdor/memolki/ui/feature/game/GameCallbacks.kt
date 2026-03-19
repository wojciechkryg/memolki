package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel

data class GameCallbacks(
    val onBackCardClick: (CardModel) -> Unit = {},
    val onFrontCardPress: (Boolean, CardModel) -> Unit = { _, _ -> },
    val onMatchAnimationComplete: () -> Unit = {},
    val onMismatchShakeComplete: () -> Unit = {}
)
