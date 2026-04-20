package com.wojdor.memolki.util.provider

expect open class AppInstalledProvider {
    open fun isAppInstalled(packageName: String): Boolean
}
