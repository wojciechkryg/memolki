package com.wojdor.memolki.util.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberNotificationPermissionRequester(
    onResult: (isGranted: Boolean) -> Unit
): NotificationPermissionRequester = remember(onResult) {
    IosNotificationPermissionRequester(onResult)
}

// TODO(kmp-ios): wire up UNUserNotificationCenter.requestAuthorization when iOS push lands.
private class IosNotificationPermissionRequester(
    private val onResult: (Boolean) -> Unit
) : NotificationPermissionRequester {
    override fun request() {
        onResult(false)
    }
}
