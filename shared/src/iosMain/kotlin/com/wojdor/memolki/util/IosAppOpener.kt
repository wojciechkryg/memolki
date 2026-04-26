package com.wojdor.memolki.util

// TODO(kmp-ios): wire to UIApplication.openURL with App Store URLs when iOS ships.
class IosAppOpener : AppOpener {
    override fun showAppInstall(appId: String) = Unit
    override fun openApp(appId: String) = Unit
}
