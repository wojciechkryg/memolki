package com.wojdor.memolki.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R

private const val SHOW_CLICK_INDICATOR = false
private const val INDICATOR_SIZE_DP = 160
private const val FADE_IN_MS = 300
private const val HOLD_MS = 200L
private const val FADE_OUT_MS = 500
private const val SCALE_UP = 1.15f
private const val SCALE_DOWN = 1f
private const val SCALE_UP_MS = 200
private const val SCALE_DOWN_MS = 300
private const val FINGERTIP_X_FRACTION = 0.18f
private const val FINGERTIP_Y_FRACTION = 0.06f

/**
 * Temporary overlay for recording demo videos. Displays a cursor image (ic_cursor.png) at each
 * tap position with a fade-in/fade-out animation. Not intended for production use.
 *
 * To enable, set [SHOW_CLICK_INDICATOR] to true. To clean up, delete this file,
 * remove the wrapper from AppActivity.kt, and delete res/drawable/ic_cursor.png.
 */
@Suppress("KotlinConstantConditions")
@Composable
fun ClickIndicatorOverlay(content: @Composable () -> Unit) {
    if (!SHOW_CLICK_INDICATOR) {
        content()
        return
    }
    val indicators = remember { mutableStateListOf<ClickIndicator>() }
    val nextId = remember { longArrayOf(0L) }
    Box(
        modifier = Modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    event.changes.forEach { change ->
                        if (change.pressed && !change.previousPressed) {
                            val id = nextId[0]++
                            indicators.add(
                                ClickIndicator(
                                    id = id,
                                    x = change.position.x,
                                    y = change.position.y
                                )
                            )
                        }
                    }
                }
            }
        }
    ) {
        content()
        indicators.forEach { indicator ->
            ClickIndicatorImage(
                indicator = indicator,
                onAnimationComplete = { indicators.remove(indicator) }
            )
        }
    }
}

@Composable
private fun ClickIndicatorImage(
    indicator: ClickIndicator,
    onAnimationComplete: () -> Unit
) {
    val sizeDp = INDICATOR_SIZE_DP.dp
    val sizePx = with(LocalDensity.current) { sizeDp.toPx() }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(SCALE_UP) }
    LaunchedEffect(indicator.id) {
        launch { alpha.animateTo(1f, animationSpec = tween(durationMillis = FADE_IN_MS)) }
        scale.animateTo(SCALE_DOWN, animationSpec = tween(durationMillis = SCALE_DOWN_MS))
        launch { scale.animateTo(SCALE_UP, animationSpec = tween(durationMillis = SCALE_UP_MS)) }
        alpha.animateTo(0f, animationSpec = tween(durationMillis = FADE_OUT_MS))
        onAnimationComplete()
    }
    val offsetX = (indicator.x - FINGERTIP_X_FRACTION * sizePx).toInt()
    val offsetY = (indicator.y - FINGERTIP_Y_FRACTION * sizePx).toInt()
    Image(
        painter = painterResource(R.drawable.ic_cursor),
        contentDescription = null,
        modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .size(sizeDp)
            .graphicsLayer {
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
            }
    )
}

private data class ClickIndicator(
    val id: Long,
    val x: Float,
    val y: Float
)
