package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiEffect
import com.wojdor.memolki.util.playgames.GooglePlayGames

sealed class MenuEffect : UiEffect {
    object OpenChooseLevelScreen : MenuEffect()
    object OpenCollectionScreen : MenuEffect()
    data class OpenLeaderboardScreen(val googlePlayGames: GooglePlayGames) : MenuEffect()
    data class SendTotalCoinsScore(
        val googlePlayGames: GooglePlayGames,
        val totalCoins: Long
    ) : MenuEffect()

    data class SendTotalCardPairsMatchedScore(
        val googlePlayGames: GooglePlayGames,
        val totalCardPairsMatched: Long
    ) : MenuEffect()

    object OpenSettingsScreen : MenuEffect()
    object OpenMoreAppsScreen : MenuEffect()
}
