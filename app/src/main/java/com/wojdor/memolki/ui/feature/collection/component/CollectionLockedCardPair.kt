package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionLockedCardPair(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
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
            Icon(
                modifier = Modifier.size(LOCKED_ICON_SIZE),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = colorResource(R.color.font)
            )
        }
    }
}

internal const val CARD_SIZE_FRACTION = 0.65f
internal const val CARD_ROTATION = 10f
internal val CARD_OFFSET = 24.dp
private val LOCKED_ICON_SIZE = 48.dp

@Preview
@Composable
fun CollectionLockedCardPairPreview() {
    AppTheme {
        CollectionLockedCardPair(Modifier.size(192.dp))
    }
}
