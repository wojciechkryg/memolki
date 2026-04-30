package com.wojdor.memolki.util.provider

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

class IosLocaleProvider : LocaleProvider {

    override fun getLanguageTag(): String {
        val override = NSUserDefaults.standardUserDefaults.stringArrayForKey(APPLE_LANGUAGES_KEY)
            ?.firstOrNull() as? String
        if (override != null) return override.languageCode()
        val bundlePreferred = NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
        if (bundlePreferred != null) return bundlePreferred.languageCode()
        val systemPreferred = NSLocale.preferredLanguages.firstOrNull() as? String
        if (systemPreferred != null) return systemPreferred.languageCode()
        return DEFAULT_LANGUAGE
    }

    override fun setLanguageTag(tag: String) {
        NSUserDefaults.standardUserDefaults.setObject(listOf(tag), APPLE_LANGUAGES_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }

    private fun String.languageCode(): String = substringBefore('-')

    private companion object {
        const val APPLE_LANGUAGES_KEY = "AppleLanguages"
        const val DEFAULT_LANGUAGE = "en"
    }
}
