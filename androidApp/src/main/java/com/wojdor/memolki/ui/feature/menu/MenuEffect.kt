package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiEffect

sealed class MenuEffect : UiEffect {
    object OpenChooseBoardScreen : MenuEffect()
    object OpenCollectionScreen : MenuEffect()
    object OpenLeaderboardScreen : MenuEffect()
    data class SendTotalCoinsScore(val totalCoins: Long) : MenuEffect()
    data class SendTotalCardPairsMatchedScore(val totalCardPairsMatched: Long) : MenuEffect()

    object OpenSettingsScreen : MenuEffect()
    object OpenMoreAppsScreen : MenuEffect()
    object OpenShopScreen : MenuEffect()
}
