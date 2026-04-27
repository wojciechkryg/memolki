package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.AppInstalledProvider

class FakeAppInstalledProvider : AppInstalledProvider {

    var mockAppInstalled = false

    override fun isAppInstalled(packageName: String): Boolean {
        return mockAppInstalled
    }
}
