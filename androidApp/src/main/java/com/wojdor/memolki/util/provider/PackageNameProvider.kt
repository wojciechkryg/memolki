package com.wojdor.memolki.util.provider

import android.content.Context

open class PackageNameProvider(
    private val context: Context
) {

    open fun providePackageName(): String {
        return context.packageName
    }
}
