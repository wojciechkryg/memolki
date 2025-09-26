package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.components.AutoSizeText
import com.wojdor.memolki.ui.shape.RotatedCardPairShape
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.throttleClick

@Composable
fun CollectionUnlockCardPairWithCoins(
    modifier: Modifier = Modifier,
    collectionCardPairModel: CollectionCardPairModel.LockedToUnlockWithCoins,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(
                RotatedCardPairShape(
                    cardShape = CardShape,
                    sizeFraction = CARD_SIZE_FRACTION,
                    rotation = CARD_ROTATION,
                    xOffset = CARD_OFFSET
                )
            )
            .clickable(
                onClick = throttleClick(onClick = onClick),
            ),
        contentAlignment = Alignment.Center
    ) {
        CollectionLockedCard(
            modifier = Modifier
                .fillMaxSize(CARD_SIZE_FRACTION)
                .rotate(CARD_ROTATION)
                .offset(x = CARD_OFFSET)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(CARD_SIZE_FRACTION)
                .rotate(-CARD_ROTATION)
                .offset(x = -CARD_OFFSET),
            contentAlignment = Alignment.Center
        ) {
            CollectionLockedCard()
            UnlockWithCoins(collectionCardPairModel)
        }
    }
}

@Composable
private fun UnlockWithCoins(
    collectionCardPairModel: CollectionCardPairModel.LockedToUnlockWithCoins
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(COIN_ICON_SIZE),
            painter = painterResource(R.drawable.ic_coin),
            contentDescription = null,
        )
        AutoSizeText(
            modifier = Modifier.padding(start = 4.dp),
            text = collectionCardPairModel.coins.toString(),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Preview
@Composable
fun CollectionUnlockCardPairWithCoinsPreview() {
    AppTheme {
        CollectionUnlockCardPairWithCoins(
            modifier = Modifier.size(192.dp),
            collectionCardPairModel = CollectionCardPairModel.LockedToUnlockWithCoins(coins = 100)
        )
    }
}

private val COIN_ICON_SIZE = 32.dp
