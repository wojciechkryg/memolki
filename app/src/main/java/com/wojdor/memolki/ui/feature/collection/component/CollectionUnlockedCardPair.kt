package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wojdor.memolki.domain.model.CollectionCardPairModel

@Composable
fun CollectionUnlockedCardPair(collectionCardPairModel: CollectionCardPairModel.Unlocked) {
    Text(collectionCardPairModel.cardPair.pair.first.pairId)
}
