package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CollectionCardPairModel

data class CollectionCallbacks(
    val onShopButtonClick: () -> Unit = {},
    val onUnlockedCardPairClick: (CollectionCardPairModel.Unlocked) -> Unit = {}
)
