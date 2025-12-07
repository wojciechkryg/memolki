package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.base.UiEffect

sealed class MenuEffect : UiEffect {
    object OpenChooseLevelScreen : MenuEffect()
    object OpenCollectionScreen : MenuEffect()
    data class OpenLeaderboardScreen(val googlePlayGames: GooglePlayGames) : MenuEffect()
    object OpenSettingsScreen : MenuEffect()
    object OpenMoreAppsScreen : MenuEffect()
}
