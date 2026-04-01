package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.mockk
import javax.inject.Inject

class FakePermissionProvider @Inject constructor() : PermissionProvider(mockk()) {

    var hasPermission = false

    override fun hasNotificationPermission(): Boolean {
        return hasPermission
    }
}
