package com.wojdor.memolki.util.provider

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IosAppInstalledProvider : AppInstalledProvider {

    override fun isAppInstalled(packageName: String): Boolean {
        val url = NSURL.URLWithString("$packageName://") ?: return false
        return UIApplication.sharedApplication.canOpenURL(url)
    }
}
