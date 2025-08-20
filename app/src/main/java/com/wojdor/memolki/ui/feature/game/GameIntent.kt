package com.wojdor.memolki.ui.feature.game

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.base.UiIntent

abstract class GameIntent : UiIntent {
    data class OnLevelStart(val levelModel: LevelModel) : GameIntent()
    data class OnCardClick(val cardModel: CardModel) : GameIntent()
}
