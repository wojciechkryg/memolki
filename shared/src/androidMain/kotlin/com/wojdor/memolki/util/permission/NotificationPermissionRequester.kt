package com.wojdor.memolki.util.permission

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberNotificationPermissionRequester(
    onResult: (isGranted: Boolean) -> Unit
): NotificationPermissionRequester {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> onResult(isGranted) }
    return remember(launcher, onResult) {
        AndroidNotificationPermissionRequester(launcher, onResult)
    }
}

private class AndroidNotificationPermissionRequester(
    private val launcher: ActivityResultLauncher<String>,
    private val onResult: (Boolean) -> Unit
) : NotificationPermissionRequester {

    override fun request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onResult(true)
        }
    }
}
