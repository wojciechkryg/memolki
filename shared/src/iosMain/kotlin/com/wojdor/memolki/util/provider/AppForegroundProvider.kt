package com.wojdor.memolki.util.provider

actual open class AppForegroundProvider actual constructor() {

    // TODO(ios): detect foreground via UIApplication.sharedApplication.applicationState.
    actual open fun isAppInForeground(): Boolean = true
}
