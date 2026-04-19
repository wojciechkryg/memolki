package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.mockk

class FakePermissionProvider : PermissionProvider(mockk()) {

    var hasPermission = false

    override fun hasNotificationPermission(): Boolean {
        return hasPermission
    }
}
