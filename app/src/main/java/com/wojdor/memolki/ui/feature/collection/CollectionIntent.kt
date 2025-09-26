package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class CollectionIntent : UiIntent {

    object OnShopClick : CollectionIntent()

    data class OnCardPairClick(
        val collectionCardPairModel: CollectionCardPairModel.Unlocked
    ) : CollectionIntent()

    data class OnUnlockWithCoinsClick(
        val collectionCardPairModel: CollectionCardPairModel.LockedToUnlockWithCoins
    ) : CollectionIntent()
}
