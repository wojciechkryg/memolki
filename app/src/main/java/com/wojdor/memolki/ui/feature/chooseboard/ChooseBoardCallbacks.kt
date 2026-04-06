package com.wojdor.memolki.ui.feature.chooseboard

import com.wojdor.memolki.domain.model.BoardModel

data class ChooseBoardCallbacks(
    val onBoardClick: (boardModel: BoardModel) -> Unit = {},
    val onDailyChallengeClick: () -> Unit = {},
    val onLockedBoardClick: () -> Unit = {}
)
