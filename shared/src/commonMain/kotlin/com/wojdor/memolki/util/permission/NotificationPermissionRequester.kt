package com.wojdor.memolki.util.permission

import androidx.compose.runtime.Composable

interface NotificationPermissionRequester {
    fun request()
}

@Composable
expect fun rememberNotificationPermissionRequester(
    onResult: (isGranted: Boolean) -> Unit
): NotificationPermissionRequester
