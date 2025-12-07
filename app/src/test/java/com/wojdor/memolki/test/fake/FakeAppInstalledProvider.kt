package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.AppInstalledProvider
import io.mockk.mockk
import javax.inject.Inject

class FakeAppInstalledProvider @Inject constructor() : AppInstalledProvider(mockk()) {

    var mockAppInstalled = false

    override fun isAppInstalled(packageName: String): Boolean {
        return mockAppInstalled
    }
}
