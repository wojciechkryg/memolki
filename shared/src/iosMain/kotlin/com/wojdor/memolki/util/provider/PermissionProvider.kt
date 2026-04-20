package com.wojdor.memolki.util.provider

actual open class PermissionProvider {

    // TODO(ios): query via UNUserNotificationCenter.currentNotificationCenter().notificationSettings.
    actual open fun hasNotificationPermission(): Boolean = false
}
