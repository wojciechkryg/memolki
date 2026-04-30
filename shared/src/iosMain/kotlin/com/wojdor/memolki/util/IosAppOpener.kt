package com.wojdor.memolki.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IosAppOpener : AppOpener {

    override fun showAppInstall(appId: String) {
        val url = NSURL.URLWithString("itms-apps://itunes.apple.com/app/$appId")
            ?: NSURL.URLWithString("https://apps.apple.com/app/$appId")
            ?: return
        UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any?>(), completionHandler = null)
    }

    override fun openApp(appId: String) {
        val url = NSURL.URLWithString("$appId://") ?: return
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any?>(), completionHandler = null)
        }
    }
}
