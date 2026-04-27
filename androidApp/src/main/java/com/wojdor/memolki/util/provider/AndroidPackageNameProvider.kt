package com.wojdor.memolki.util.provider

import android.content.Context

class AndroidPackageNameProvider(
    private val context: Context
) : PackageNameProvider {

    override fun providePackageName(): String = context.packageName
}
