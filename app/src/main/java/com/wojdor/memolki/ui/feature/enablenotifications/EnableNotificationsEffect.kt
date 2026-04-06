package com.wojdor.memolki.ui.feature.enablenotifications

import com.wojdor.memolki.ui.base.UiEffect

sealed class EnableNotificationsEffect : UiEffect {
    object RequestNotificationPermission : EnableNotificationsEffect()
    data class NavigateToGame(val boardId: String) : EnableNotificationsEffect()
    object NavigateToMenu : EnableNotificationsEffect()
    object NavigateToCollection : EnableNotificationsEffect()
    object NavigateToShop : EnableNotificationsEffect()
}
