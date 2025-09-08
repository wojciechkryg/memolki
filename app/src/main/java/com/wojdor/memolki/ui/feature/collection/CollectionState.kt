package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionState(
    val coins: Long = 0L,
    val collectionCardPairs: List<CollectionCardPairModel> = emptyList(),
    val allCardPairsCount: Int = 0,
    val unlockedCardPairsCount: Int = 0
) : UiState
