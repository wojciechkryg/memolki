package com.wojdor.memolki.ui.app

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val EDGE_WIDTH = 24.dp
private val TRIGGER_DISTANCE = 80.dp

fun Modifier.iosEdgeSwipeBack(
    layoutDirection: LayoutDirection,
    onSwipeBack: () -> Unit,
): Modifier = pointerInput(layoutDirection, onSwipeBack) {
    val edgeWidthPx = EDGE_WIDTH.toPx()
    val triggerPx = TRIGGER_DISTANCE.toPx()
    val isLtr = layoutDirection == LayoutDirection.Ltr
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        val onLeadingEdge = if (isLtr) {
            down.position.x < edgeWidthPx
        } else {
            down.position.x > size.width - edgeWidthPx
        }
        if (!onLeadingEdge) return@awaitEachGesture
        val startX = down.position.x
        var triggered = false
        while (true) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull { it.id == down.id } ?: break
            val dx = change.position.x - startX
            val advanced = if (isLtr) dx > triggerPx else dx < -triggerPx
            if (!triggered && advanced) {
                change.consume()
                triggered = true
                onSwipeBack()
                break
            }
            if (!change.pressed) break
        }
    }
}
