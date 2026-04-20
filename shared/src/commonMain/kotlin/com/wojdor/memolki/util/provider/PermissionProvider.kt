package com.wojdor.memolki.util.provider

expect open class PermissionProvider {
    open fun hasNotificationPermission(): Boolean
}
