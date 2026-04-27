package com.wojdor.memolki.util.provider

class IosAppInstalledProvider : AppInstalledProvider {

    // TODO(ios): check via UIApplication.canOpenURL with a URL scheme (requires LSApplicationQueriesSchemes).
    override fun isAppInstalled(packageName: String): Boolean = false
}
