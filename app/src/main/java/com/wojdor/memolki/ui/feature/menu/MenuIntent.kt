package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiIntent

sealed class MenuIntent : UiIntent {
    object OnNewGameClicked : MenuIntent()
    object OnCollectionClicked : MenuIntent()
    object OnSettingsClicked : MenuIntent()
}
