package com.wojdor.memolki.util.provider

class IosLocaleProvider : LocaleProvider {

    private var currentTag: String = "en"

    // TODO(ios): read from NSLocale.currentLocale.languageCode and/or NSUserDefaults AppleLanguages.
    override fun getLanguageTag(): String = currentTag

    // TODO(ios): persist to NSUserDefaults AppleLanguages and rewarm app locale.
    override fun setLanguageTag(tag: String) {
        currentTag = tag
    }
}
