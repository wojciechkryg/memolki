package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.serialization.Serializable

@Serializable
data class CollectionState(
    val coins: Long = 0L,
    val collectionCardPairs: List<CollectionCardPairModel> = emptyList(),
    val animateCoins: Boolean = true
) : UiState {

    val allCardPairsCount
        get() = collectionCardPairs.size

    val unlockedCardPairsCount
        get() = collectionCardPairs.filter { it is CollectionCardPairModel.Unlocked }.size
}
