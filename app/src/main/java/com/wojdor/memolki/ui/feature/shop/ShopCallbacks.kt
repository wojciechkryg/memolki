package com.wojdor.memolki.ui.feature.shop

data class ShopCallbacks(
    val onWatchAdClick: () -> Unit = {},
    val onBuyCoinsSmallAmountClick: () -> Unit = {},
    val onBuyCoinsBigAmountClick: () -> Unit = {},
    val onBuyAllCardsClick: () -> Unit = {}
)
