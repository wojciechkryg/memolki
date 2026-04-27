package com.wojdor.memolki.util.provider

interface AppInstalledProvider {
    fun isAppInstalled(packageName: String): Boolean
}
