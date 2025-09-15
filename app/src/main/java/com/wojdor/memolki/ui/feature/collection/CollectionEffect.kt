package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class CollectionEffect : UiEffect {
    object OpenShopScreen : CollectionEffect()
    data class OpenCardPairDetailsScreen(val cardPairModel: CardPairModel) : CollectionEffect()
}
