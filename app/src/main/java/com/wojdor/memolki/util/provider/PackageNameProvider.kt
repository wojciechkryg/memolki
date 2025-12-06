package com.wojdor.memolki.util.provider

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackageNameProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun providePackageName(): String {
        Log.d("TESTWOJDOR", "current: ${context.packageName}")
        return context.packageName
    }
}
