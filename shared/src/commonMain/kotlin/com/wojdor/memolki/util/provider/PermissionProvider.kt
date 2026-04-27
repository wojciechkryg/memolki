package com.wojdor.memolki.util.provider

interface PermissionProvider {
    fun hasNotificationPermission(): Boolean
}
