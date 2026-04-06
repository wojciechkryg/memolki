package com.wojdor.memolki.ui.feature.chooseboard

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class ChooseBoardEffect : UiEffect {
    data class OpenGameScreen(val boardModel: BoardModel) : ChooseBoardEffect()
    object OpenDailyChallengeScreen : ChooseBoardEffect()
    object OpenCollectionScreen : ChooseBoardEffect()
}
