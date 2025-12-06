package com.wojdor.memolki.util.provider

import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class AppInstalledProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    open fun isAppInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (error: PackageManager.NameNotFoundException) {
            false
        }
    }
}
