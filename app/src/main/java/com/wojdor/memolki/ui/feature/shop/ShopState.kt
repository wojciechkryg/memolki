package com.wojdor.memolki.ui.feature.shop

import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopState(
    val coins: Long = 0L,
    val animateCoins: Boolean = true
) : UiState
