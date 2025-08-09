package com.wojdor.memolki.ui.menu

import com.wojdor.memolki.ui.base.UiIntent

sealed class MenuIntent : UiIntent {
    object OnStartGameClicked : MenuIntent()
    object OnOptionsClicked : MenuIntent()
}
