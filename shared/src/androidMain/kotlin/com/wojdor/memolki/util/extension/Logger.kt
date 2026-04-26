@file:JvmName("LoggerAndroid")

package com.wojdor.memolki.util.extension

import android.util.Log
import kotlin.jvm.JvmName

internal actual fun logD(tag: String, message: String) {
    Log.d(tag, message)
}

internal actual fun logE(tag: String, message: String, throwable: Throwable) {
    runCatching { Log.e(tag, message, throwable) }
}
