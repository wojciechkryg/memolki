package com.wojdor.memolki.util.provider

import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationState

class IosAppForegroundProvider : AppForegroundProvider {

    override fun isAppInForeground(): Boolean =
        UIApplication.sharedApplication.applicationState == UIApplicationState.UIApplicationStateActive
}
