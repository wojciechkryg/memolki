package com.wojdor.memolki.util.provider

expect open class AppForegroundProvider() {
    open fun isAppInForeground(): Boolean
}
