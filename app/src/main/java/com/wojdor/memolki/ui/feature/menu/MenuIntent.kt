package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiIntent

sealed class MenuIntent : UiIntent {
    object OnNewGameClick : MenuIntent()
    object OnCollectionClick : MenuIntent()
    object OnSettingsClick : MenuIntent()
}
