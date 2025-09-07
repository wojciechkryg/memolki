package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionState(
    val coins: Long = 0L,
    val unlockedCardPairs: List<CardPairModel> = emptyList(),
    val lockedCardPairsCount: Int = 0
) : UiState
