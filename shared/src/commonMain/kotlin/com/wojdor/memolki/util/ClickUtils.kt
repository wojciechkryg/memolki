package com.wojdor.memolki.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.TimeSource

private const val THROTTLE_TIME_MS = 1000L

@Composable
fun throttleClick(
    onClick: () -> Unit,
    throttleTimeMs: Long = THROTTLE_TIME_MS
): () -> Unit {
    var lastClickMark by remember { mutableStateOf<TimeSource.Monotonic.ValueTimeMark?>(null) }
    return {
        val now = TimeSource.Monotonic.markNow()
        val previous = lastClickMark
        if (previous == null || (now - previous).inWholeMilliseconds > throttleTimeMs) {
            lastClickMark = now
            onClick()
        }
    }
}
