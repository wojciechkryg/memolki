package com.wojdor.memolki.util.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

@Composable
actual fun rememberNotificationPermissionRequester(
    onResult: (isGranted: Boolean) -> Unit
): NotificationPermissionRequester = remember(onResult) {
    IosNotificationPermissionRequester(onResult)
}

private class IosNotificationPermissionRequester(
    private val onResult: (Boolean) -> Unit
) : NotificationPermissionRequester {
    override fun request() {
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or
                UNAuthorizationOptionBadge or
                UNAuthorizationOptionSound,
            completionHandler = { granted, _ -> onResult(granted) }
        )
    }
}
