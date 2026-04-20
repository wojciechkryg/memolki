package com.wojdor.memolki.util.provider

import android.content.Context

actual open class PackageNameProvider(
    private val context: Context
) {

    actual open fun providePackageName(): String = context.packageName
}
