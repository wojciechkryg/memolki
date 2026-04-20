package com.wojdor.memolki.util.provider

import android.content.Context
import android.content.pm.PackageManager

actual open class AppInstalledProvider(
    private val context: Context
) {

    actual open fun isAppInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (error: PackageManager.NameNotFoundException) {
            false
        }
    }
}
