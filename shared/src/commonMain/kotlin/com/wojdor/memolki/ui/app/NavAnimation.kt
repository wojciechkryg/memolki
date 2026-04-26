package com.wojdor.memolki.ui.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset

private const val ANIMATION_TIME = 500
private val animationSpec = tween<IntOffset>(durationMillis = ANIMATION_TIME)

private fun slideInHorizontallyWithSpec(
    initialOffsetX: (fullWidth: Int) -> Int,
) = slideInHorizontally(initialOffsetX = initialOffsetX, animationSpec = animationSpec)

private fun slideOutHorizontallyWithSpec(
    targetOffsetX: (fullWidth: Int) -> Int,
) = slideOutHorizontally(targetOffsetX = targetOffsetX, animationSpec = animationSpec)

private fun slideInVerticallyWithSpec(
    initialOffsetY: (fullHeight: Int) -> Int,
) = slideInVertically(initialOffsetY = initialOffsetY, animationSpec = animationSpec)

private fun slideOutVerticallyWithSpec(
    targetOffsetY: (fullHeight: Int) -> Int,
) = slideOutVertically(targetOffsetY = targetOffsetY, animationSpec = animationSpec)

internal val slideInLeft = slideInHorizontallyWithSpec(initialOffsetX = { -it })
internal val slideInRight = slideInHorizontallyWithSpec(initialOffsetX = { it })
internal val slideOutLeft = slideOutHorizontallyWithSpec(targetOffsetX = { -it })
internal val slideOutRight = slideOutHorizontallyWithSpec(targetOffsetX = { it })

internal val slideInTop = slideInVerticallyWithSpec(initialOffsetY = { -it })
internal val slideInBottom = slideInVerticallyWithSpec(initialOffsetY = { it })
internal val slideOutTop = slideOutVerticallyWithSpec(targetOffsetY = { -it })
internal val slideOutBottom = slideOutVerticallyWithSpec(targetOffsetY = { it })
