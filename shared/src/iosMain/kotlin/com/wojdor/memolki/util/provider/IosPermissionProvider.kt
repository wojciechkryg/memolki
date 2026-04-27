package com.wojdor.memolki.util.provider

class IosPermissionProvider : PermissionProvider {

    // TODO(ios): query via UNUserNotificationCenter.currentNotificationCenter().notificationSettings.
    override fun hasNotificationPermission(): Boolean = false
}
