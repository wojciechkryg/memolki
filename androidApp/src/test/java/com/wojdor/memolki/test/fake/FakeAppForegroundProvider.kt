package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.AppForegroundProvider

class FakeAppForegroundProvider : AppForegroundProvider() {

    var isInForeground: Boolean = false

    override fun isAppInForeground(): Boolean = isInForeground
}
