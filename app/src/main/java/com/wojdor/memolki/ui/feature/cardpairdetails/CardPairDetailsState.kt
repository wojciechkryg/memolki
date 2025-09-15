package com.wojdor.memolki.ui.feature.cardpairdetails

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardPairDetailsState(
    val cardPairModel: CardPairModel = CardPairModel(CardModel.Empty to CardModel.Empty)
) : UiState
