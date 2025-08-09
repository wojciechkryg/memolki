package com.wojdor.memolki.ui.menu

import com.wojdor.memolki.ui.base.UiEffect

sealed class MenuEffect : UiEffect {
    object OpenChooseLevelScreen : MenuEffect()
    object OpenOptionsScreen : MenuEffect()
}
