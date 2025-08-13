package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.UiEffect

sealed class MenuEffect : UiEffect {
    object OpenChooseLevelScreen : MenuEffect()
    object OpenCollectionScreen : MenuEffect()
    object OpenSettingsScreen : MenuEffect()
}
