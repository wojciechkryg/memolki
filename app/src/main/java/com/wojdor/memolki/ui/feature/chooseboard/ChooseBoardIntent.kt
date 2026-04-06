package com.wojdor.memolki.ui.feature.chooseboard

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class ChooseBoardIntent : UiIntent {
    data class OnBoardClick(val boardModel: BoardModel) : ChooseBoardIntent()
    object OnDailyChallengeClick : ChooseBoardIntent()
}
