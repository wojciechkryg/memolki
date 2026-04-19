package com.wojdor.memolki.util.provider

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

open class LocaleProvider(
    private val context: Context
) {

    open fun getLanguageTag(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val locales = context.getSystemService(LocaleManager::class.java).applicationLocales
            if (locales.isEmpty) null else locales[0]
        } else {
            val locales = AppCompatDelegate.getApplicationLocales()
            if (locales.isEmpty) null else locales[0]
        }
        return locale?.language ?: Locale.getDefault().language
    }

    open fun setLanguageTag(tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales = LocaleList.forLanguageTags(tag)
        } else {
            val localeList = LocaleListCompat.forLanguageTags(tag)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

}
