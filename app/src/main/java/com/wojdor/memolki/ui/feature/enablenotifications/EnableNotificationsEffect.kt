package com.wojdor.memolki.ui.feature.enablenotifications

import com.wojdor.memolki.ui.base.UiEffect

sealed class EnableNotificationsEffect : UiEffect {
    object RequestNotificationPermission : EnableNotificationsEffect()
    object NavigateToGame : EnableNotificationsEffect()
    object NavigateToMenu : EnableNotificationsEffect()
    object NavigateToCollection : EnableNotificationsEffect()
}
