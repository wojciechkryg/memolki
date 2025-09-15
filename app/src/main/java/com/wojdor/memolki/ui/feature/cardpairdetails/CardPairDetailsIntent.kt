package com.wojdor.memolki.ui.feature.cardpairdetails

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class CardPairDetailsIntent : UiIntent {
    data class OnCardPairDetailsShow(val cardPairModel: CardPairModel) : CardPairDetailsIntent()
}
