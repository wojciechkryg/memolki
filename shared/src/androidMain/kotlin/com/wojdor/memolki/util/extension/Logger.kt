package com.wojdor.memolki.util.extension

import android.util.Log
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

actual fun Any.logD(message: String) {
    Log.d(this::class.simpleName, message)
}

actual fun Any.logE(message: String, throwable: Throwable) {
    val tag = this::class.simpleName ?: "Unknown"
    runCatching { Log.e(tag, message, throwable) }
    runCatching {
        Firebase.crashlytics.apply {
            setCustomKey("source", tag)
            log("[$tag] $message")
            recordException(throwable)
        }
    }
}
