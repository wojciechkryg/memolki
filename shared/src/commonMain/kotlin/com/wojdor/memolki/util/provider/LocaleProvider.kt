package com.wojdor.memolki.util.provider

expect open class LocaleProvider {
    open fun getLanguageTag(): String
    open fun setLanguageTag(tag: String)
}
