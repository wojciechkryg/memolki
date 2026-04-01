package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class GameIntent : UiIntent {
    data class OnLevelStart(val levelId: String) : GameIntent()
    data class OnBackCardClick(val cardModel: CardModel) : GameIntent()
    data class OnFrontCardPress(val isPressed: Boolean, val cardModel: CardModel) : GameIntent()
    object OnMatchAnimationComplete : GameIntent()
    object OnMismatchShakeComplete : GameIntent()
    object OnGameLeave : GameIntent()
}
