package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.LocaleProvider

class FakeLocaleProvider : LocaleProvider {

    private var languageTag = DEFAULT_LANGUAGE_TAG
    var shouldThrowOnSet = false

    override fun getLanguageTag(): String {
        return languageTag
    }

    override fun setLanguageTag(tag: String) {
        if (shouldThrowOnSet) throw RuntimeException("Locale change failed")
        languageTag = tag
    }

    companion object {
        private const val DEFAULT_LANGUAGE_TAG = "en"
    }
}
