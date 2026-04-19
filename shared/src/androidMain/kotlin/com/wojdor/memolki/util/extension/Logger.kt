package com.wojdor.memolki.util.extension

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

actual fun Any.logD(message: String) {
    Log.d(this::class.simpleName, message)
}

actual fun Any.logE(message: String, throwable: Throwable) {
    val tag = this::class.simpleName ?: "Unknown"
    Log.e(tag, message, throwable)
    FirebaseCrashlytics.getInstance().apply {
        setCustomKey("source", tag)
        log("[$tag] $message")
        recordException(throwable)
    }
}
