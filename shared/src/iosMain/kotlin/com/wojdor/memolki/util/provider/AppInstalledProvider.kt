package com.wojdor.memolki.util.provider

actual open class AppInstalledProvider {

    // TODO(ios): check via UIApplication.canOpenURL with a URL scheme (requires LSApplicationQueriesSchemes).
    actual open fun isAppInstalled(packageName: String): Boolean = false
}
