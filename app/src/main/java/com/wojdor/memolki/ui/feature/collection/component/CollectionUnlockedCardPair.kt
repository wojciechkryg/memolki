package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.feature.collection.getCollectionStateForPreview
import com.wojdor.memolki.ui.feature.game.component.FrontCardItem
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionUnlockedCardPair(
    modifier: Modifier = Modifier,
    collectionCardPairModel: CollectionCardPairModel.Unlocked
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FrontCardItem(
            modifier = Modifier
                .fillMaxSize(CARD_SIZE_FRACTION)
                .rotate(CARD_ROTATION)
                .offset(x = CARD_OFFSET),
            card = collectionCardPairModel.cardPair.pair.second
        )
        FrontCardItem(
            modifier = Modifier
                .fillMaxSize(CARD_SIZE_FRACTION)
                .rotate(-CARD_ROTATION)
                .offset(x = -CARD_OFFSET),
            card = collectionCardPairModel.cardPair.pair.first
        )

    }
}

@Preview
@Composable
fun CollectionUnlockedCardPairPreview() {
    AppTheme {
        CollectionUnlockedCardPair(
            modifier = Modifier.size(256.dp),
            collectionCardPairModel = getCollectionStateForPreview()
                .collectionCardPairs
                .first { it is CollectionCardPairModel.Unlocked } as CollectionCardPairModel.Unlocked
        )
    }
}
