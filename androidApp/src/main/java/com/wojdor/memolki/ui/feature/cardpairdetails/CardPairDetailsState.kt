package com.wojdor.memolki.ui.feature.cardpairdetails

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardPairDetailsState(
    val cardPairModels: List<CardPairModel> = emptyList(),
    val initialPage: Int = 0
) : UiState
