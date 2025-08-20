package com.wojdor.memolki.util

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private const val THROTTLE_TIME_MS = 1000L

@Composable
fun rememberThrottleClick(
    onClick: () -> Unit,
    throttleTimeMs: Long = THROTTLE_TIME_MS
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > throttleTimeMs) {
            lastClickTime = currentTime
            onClick()
        }
    }
}
