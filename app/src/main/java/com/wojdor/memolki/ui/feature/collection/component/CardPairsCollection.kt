package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.feature.collection.CollectionState

@Composable
fun CardPairsCollection(state: CollectionState) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.collectionCardPairs) { collectionCardPair ->
                Box(modifier = Modifier.size(cardPairSize), contentAlignment = Alignment.Center) {
                    when (collectionCardPair) {
                        is CollectionCardPairModel.Unlocked -> CollectionUnlockedCardPair(
                            collectionCardPair
                        )

                        is CollectionCardPairModel.Locked -> CollectionLockedCardPair()
                        is CollectionCardPairModel.LockedToUnlockWithAd -> CollectionLockedCardPair()
                        is CollectionCardPairModel.LockedToUnlockWithCoins -> CollectionLockedCardPair()
                    }
                }
            }
        }
    }
}
