package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.shape.RotatedCardPairShape
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.throttleClick

@Composable
fun CollectionUnlockCardPairWithAd(
    modifier: Modifier = Modifier,
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
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(R.drawable.ic_ads),
                    contentDescription = null,
                )
                AutoSizeText(
                    text = stringResource(R.string.watch_ad).uppercase(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun CollectionUnlockCardPairWithAdPreview() {
    AppTheme {
        CollectionUnlockCardPairWithAd(
            modifier = Modifier.size(192.dp),
        )
    }
}

