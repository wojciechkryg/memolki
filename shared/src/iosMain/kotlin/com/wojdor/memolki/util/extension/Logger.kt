package com.wojdor.memolki.util.extension

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

actual fun Any.logD(message: String) {
    val tag = this::class.simpleName ?: "Unknown"
    println("D/$tag: $message")
}

actual fun Any.logE(message: String, throwable: Throwable) {
    val tag = this::class.simpleName ?: "Unknown"
    println("E/$tag: $message\n${throwable.stackTraceToString()}")
    runCatching {
        Firebase.crashlytics.apply {
            setCustomKey("source", tag)
            log("[$tag] $message")
            recordException(throwable)
        }
    }
}
