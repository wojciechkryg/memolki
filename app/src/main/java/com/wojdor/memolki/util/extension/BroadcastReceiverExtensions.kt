package com.wojdor.memolki.util.extension

import android.content.BroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun BroadcastReceiver.goAsyncIo(errorMessage: String, block: suspend () -> Unit) {
    val pendingResult = goAsync()
    CoroutineScope(Dispatchers.IO).launch {
        try {
            block()
        } catch (e: Exception) {
            logE(errorMessage, e)
        } finally {
            pendingResult.finish()
        }
    }
}
