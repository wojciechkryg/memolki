package com.wojdor.memolki.util.provider

import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

class IosPermissionProvider : PermissionProvider {

    private var cachedAuthorized: Boolean = true

    init {
        refresh()
    }

    override fun hasNotificationPermission(): Boolean {
        refresh()
        return cachedAuthorized
    }

    private fun refresh() {
        UNUserNotificationCenter.currentNotificationCenter()
            .getNotificationSettingsWithCompletionHandler { settings ->
                val status = settings?.authorizationStatus
                cachedAuthorized = status == UNAuthorizationStatusAuthorized ||
                    status == UNAuthorizationStatusProvisional ||
                    status == UNAuthorizationStatusEphemeral
            }
    }
}
