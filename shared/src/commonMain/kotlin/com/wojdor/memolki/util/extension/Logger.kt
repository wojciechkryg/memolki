package com.wojdor.memolki.util.extension

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

internal expect fun logD(tag: String, message: String)

internal expect fun logE(tag: String, message: String, throwable: Throwable)

fun Any.logD(message: String) {
    logD(tagName(), message)
}

fun Any.logE(message: String, throwable: Throwable) {
    val tag = tagName()
    logE(tag, message, throwable)
    runCatching {
        Firebase.crashlytics.apply {
            setCustomKey("source", tag)
            log("[$tag] $message")
            recordException(throwable)
        }
    }
}

private fun Any.tagName(): String = this::class.simpleName ?: "Unknown"
