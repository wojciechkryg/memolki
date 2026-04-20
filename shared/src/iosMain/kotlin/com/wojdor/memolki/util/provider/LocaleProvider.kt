package com.wojdor.memolki.util.provider

actual open class LocaleProvider {

    private var currentTag: String = "en"

    // TODO(ios): read from NSLocale.currentLocale.languageCode and/or NSUserDefaults AppleLanguages.
    actual open fun getLanguageTag(): String = currentTag

    // TODO(ios): persist to NSUserDefaults AppleLanguages and rewarm app locale.
    actual open fun setLanguageTag(tag: String) {
        currentTag = tag
    }
}
