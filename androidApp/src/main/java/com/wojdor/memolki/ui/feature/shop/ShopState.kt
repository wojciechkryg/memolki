package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopState(
    val coins: Long = 0L,
    val menu: List<ShopMenuModel> = emptyList(),
    val animateCoins: Boolean = true
) : UiState
