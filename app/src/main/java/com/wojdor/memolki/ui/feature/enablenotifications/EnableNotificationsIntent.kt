package com.wojdor.memolki.ui.feature.enablenotifications

import com.wojdor.memolki.ui.base.UiIntent

sealed class EnableNotificationsIntent : UiIntent {
    object OnEnableClick : EnableNotificationsIntent()
    object OnLaterClick : EnableNotificationsIntent()
    data class OnPermissionResult(val isGranted: Boolean) : EnableNotificationsIntent()
}
