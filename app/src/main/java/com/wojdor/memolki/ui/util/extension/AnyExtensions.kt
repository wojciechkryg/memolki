package com.wojdor.memolki.ui.util.extension

import android.util.Log

fun Any.logD(message: String) {
    Log.d(this::class.simpleName, message)
}

fun Any.logE(message: String, throwable: Throwable) {
    Log.e(this::class.simpleName, message, throwable)
}
