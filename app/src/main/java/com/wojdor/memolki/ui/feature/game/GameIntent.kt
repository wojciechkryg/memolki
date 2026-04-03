package com.wojdor.memolki.ui.feature.game

import android.app.Activity
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class GameIntent : UiIntent {
    data class OnLevelStart(val levelId: String, val isDailyChallenge: Boolean = false) :
        GameIntent()

    data class OnBackCardClick(val cardModel: CardModel) : GameIntent()
    data class OnFrontCardPress(val isPressed: Boolean, val cardModel: CardModel) : GameIntent()
    object OnMatchAnimationComplete : GameIntent()
    object OnMistakeShakeComplete : GameIntent()
    object OnGameLeave : GameIntent()
    data class OnSubmitTotalCardPairsMatched(val activity: Activity, val totalCardPairsMatched: Long) : GameIntent()
}
