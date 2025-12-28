package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.component.FadeEffectBottom
import com.wojdor.memolki.ui.component.FadeEffectTop
import com.wojdor.memolki.ui.component.Flippable
import com.wojdor.memolki.ui.feature.collection.CollectionCallbacks
import com.wojdor.memolki.ui.feature.collection.CollectionState
import kotlinx.coroutines.delay

@Composable
fun CardPairsCollection(
    state: CollectionState,
    callbacks: CollectionCallbacks
) {
    var isClickBlocked by remember { mutableStateOf(false) }
    val groupThrottleCallbacks = remember(callbacks) {
        callbacks.copy(
            onUnlockedCardPairClick = {
                if (!isClickBlocked) {
                    isClickBlocked = true
                    callbacks.onUnlockedCardPairClick(it)
                }
            }
        )
    }
    LaunchedEffect(isClickBlocked) {
        if (isClickBlocked) {
            delay(ON_FRONT_CARDS_CLICK_THROTTLE)
            isClickBlocked = false
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val spacing = 12.dp
        val columns = 2
        val shorterEdge = maxWidth.coerceAtMost(maxHeight)
        val cardPairSize = (shorterEdge - spacing * (columns - 1)) / columns
        val lazyGridState = rememberLazyGridState()
        val firstLockedToUnlockWithCoinsIndex = remember(state.collectionCardPairs) {
            state.collectionCardPairs.indexOfFirst {
                it is CollectionCardPairModel.LockedToUnlockWithCoins
            } - columns
        }
        val spacingInPixels = with(LocalDensity.current) { spacing.toPx() }
        val cardPairSizeInPixels = with(LocalDensity.current) { cardPairSize.toPx() }
        var hasScrolled by remember {
            mutableStateOf(lazyGridState.firstVisibleItemIndex != 0)
        }

        LaunchedEffect(firstLockedToUnlockWithCoinsIndex) {
            if (firstLockedToUnlockWithCoinsIndex > INDEX_NOT_FOUND && !hasScrolled) {
                hasScrolled = true
                delay(SCROLL_DELAY)
                val rowIndex = firstLockedToUnlockWithCoinsIndex / columns
                val targetOffset = rowIndex * (cardPairSizeInPixels + spacingInPixels)
                lazyGridState.scroll {
                    var previousValue = lazyGridState.firstVisibleItemScrollOffset.toFloat()
                    Animatable(previousValue).animateTo(
                        targetValue = targetOffset,
                        animationSpec = tween(durationMillis = SCROLL_DURATION)
                    ) {
                        val delta = value - previousValue
                        scrollBy(delta)
                        previousValue = value
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(columns),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.collectionCardPairs) { collectionCardPair ->
                    CollectionCardPair(
                        modifier = Modifier.width(cardPairSize),
                        collectionCardPair = collectionCardPair,
                        callbacks = groupThrottleCallbacks
                    )
                }
            }
            FadeEffectTop(Modifier.align(Alignment.TopCenter))
            FadeEffectBottom(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun CollectionCardPair(
    modifier: Modifier = Modifier,
    collectionCardPair: CollectionCardPairModel,
    callbacks: CollectionCallbacks
) {
    var backCardPair by remember {
        mutableStateOf<CollectionCardPairModel>(CollectionCardPairModel.Locked)
    }
    val isUnlocked = collectionCardPair is CollectionCardPairModel.Unlocked

    if (!isUnlocked) {
        backCardPair = collectionCardPair
    }

    Flippable(
        modifier = modifier,
        isFlipped = isUnlocked,
        frontSide = { modifier ->
            FrontSide(
                modifier = modifier,
                collectionCardPair = collectionCardPair,
                callbacks = callbacks
            )
        },
        backSide = { modifier ->
            BackSide(
                modifier = modifier,
                collectionCardPair = if (isUnlocked) backCardPair else collectionCardPair,
                callbacks = callbacks
            )
        }
    )
}

@Composable
private fun FrontSide(
    modifier: Modifier,
    collectionCardPair: CollectionCardPairModel,
    callbacks: CollectionCallbacks
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        (collectionCardPair as? CollectionCardPairModel.Unlocked)?.let { unlockedCard ->
            CollectionUnlockedCardPair(
                collectionCardPairModel = unlockedCard,
                onClick = { callbacks.onUnlockedCardPairClick(unlockedCard) }
            )
        }
    }
}

@Composable
private fun BackSide(
    modifier: Modifier,
    collectionCardPair: CollectionCardPairModel,
    callbacks: CollectionCallbacks
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = collectionCardPair,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = ALPHA_DURATION)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = ALPHA_DURATION))
            },
            label = "alpha animation"
        ) { targetState ->
            when (targetState) {
                is CollectionCardPairModel.Locked -> CollectionLockedCardPair()
                is CollectionCardPairModel.LockedToUnlockWithCoins ->
                    CollectionUnlockCardPairWithCoins(
                        collectionCardPairModel = targetState,
                        onClick = { callbacks.onUnlockWithCoinsClick(targetState) }
                    )

                is CollectionCardPairModel.LockedToUnlockWithAd -> CollectionUnlockCardPairWithAd(
                    onClick = { callbacks.onUnlockWithAdClick(targetState) }
                )

                is CollectionCardPairModel.Unlocked -> {
                    // handled in FrontSide
                }
            }
        }
    }
}

private const val ALPHA_DURATION = 300
private const val ON_FRONT_CARDS_CLICK_THROTTLE = 500L
private const val INDEX_NOT_FOUND = -1
private const val SCROLL_DELAY = 500L
private const val SCROLL_DURATION = 800
