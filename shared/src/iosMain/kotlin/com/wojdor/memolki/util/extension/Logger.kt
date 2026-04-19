package com.wojdor.memolki.util.extension

actual fun Any.logD(message: String) {
    val tag = this::class.simpleName ?: "Unknown"
    println("D/$tag: $message")
}

actual fun Any.logE(message: String, throwable: Throwable) {
    val tag = this::class.simpleName ?: "Unknown"
    println("E/$tag: $message\n${throwable.stackTraceToString()}")
    // TODO(kmp): report to Crashlytics on iOS too. Lands in Phase 9 when GitLive Firebase KMP is wired up.
}
