package com.wojdor.memolki.util.provider

interface LocaleProvider {
    fun getLanguageTag(): String
    fun setLanguageTag(tag: String)
}
