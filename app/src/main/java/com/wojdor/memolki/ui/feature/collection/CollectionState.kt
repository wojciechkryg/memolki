package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionState(
    val coins: Long = 0L
) : UiState
