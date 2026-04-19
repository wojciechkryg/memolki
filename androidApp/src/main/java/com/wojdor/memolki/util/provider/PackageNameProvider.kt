package com.wojdor.memolki.util.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class PackageNameProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    open fun providePackageName(): String {
        return context.packageName
    }
}
