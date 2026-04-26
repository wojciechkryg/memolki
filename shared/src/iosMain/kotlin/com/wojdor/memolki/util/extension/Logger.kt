package com.wojdor.memolki.util.extension

internal actual fun logD(tag: String, message: String) {
    println("D/$tag: $message")
}

internal actual fun logE(tag: String, message: String, throwable: Throwable) {
    println("E/$tag: $message\n${throwable.stackTraceToString()}")
}
