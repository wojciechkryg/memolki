package com.wojdor.memolki.util.provider

class IosAppForegroundProvider : AppForegroundProvider {

    // TODO(ios): detect foreground via UIApplication.sharedApplication.applicationState.
    override fun isAppInForeground(): Boolean = true
}
