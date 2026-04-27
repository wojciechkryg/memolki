package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PermissionProvider

class FakePermissionProvider : PermissionProvider {

    var hasPermission = false

    override fun hasNotificationPermission(): Boolean {
        return hasPermission
    }
}
