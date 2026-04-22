package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiIntent

sealed class MenuIntent : UiIntent {
    object OnPlayClick : MenuIntent()
    object OnCollectionClick : MenuIntent()
    object OnLeaderboardClick : MenuIntent()
    object OnSettingsClick : MenuIntent()
    object OnMoreAppsClick : MenuIntent()
    object OnDailyRewardClick : MenuIntent()
    object OnScreenResume : MenuIntent()
}
