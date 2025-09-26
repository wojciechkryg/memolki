package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.components.Flippable
import com.wojdor.memolki.ui.feature.collection.CollectionCallbacks
import com.wojdor.memolki.ui.feature.collection.CollectionState

@Composable
fun CardPairsCollection(
    state: CollectionState,
    callbacks: CollectionCallbacks
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val spacing = 8.dp
        val columns = 2
        val shorterEdge = maxWidth.coerceAtMost(maxHeight)
        val cardPairSize = (shorterEdge - spacing * (columns - 1)) / columns

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.collectionCardPairs) { collectionCardPair ->
                    CollectionCardPair(
                        modifier = Modifier.size(cardPairSize),
                        collectionCardPair = collectionCardPair,
                        callbacks = callbacks
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

                is CollectionCardPairModel.LockedToUnlockWithAd -> CollectionLockedCardPair()
                is CollectionCardPairModel.Unlocked -> { /* handled in FrontSide */
                }
            }
        }
    }
}

@Composable
private fun FadeEffectTop(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FADE_EFFECT_HEIGHT)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.primary),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
private fun FadeEffectBottom(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FADE_EFFECT_HEIGHT)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        colorResource(R.color.primary)
                    )
                )
            )
    )
}

private val FADE_EFFECT_HEIGHT = 6.dp
private const val ALPHA_DURATION = 300
