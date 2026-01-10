package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.component.CARD_BORDER_SIZE
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.isTablet
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingXS
import com.wojdor.memolki.util.throttleClick

@Composable
fun CollectionUnlockCardPairWithCoins(
    modifier: Modifier = Modifier,
    collectionCardPairModel: CollectionCardPairModel.LockedToUnlockWithCoins,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isTablet) spacingL else 0.dp)
            .clip(rotatedCardPairShape)
            .clickable(
                onClick = throttleClick(onClick = onClick),
            )
            .padding(vertical = CARD_PAIR_VERTICAL_PADDING),
        contentAlignment = Alignment.Center
    ) {
        CollectionLockedCard(
            modifier = cardLeftModifier
        )
        Box(
            modifier = cardRightModifier,
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
        modifier = Modifier
            .fillMaxSize()
            .padding(CARD_BORDER_SIZE)
            .background(color = Color.White.copy(alpha = 0.5f), CardShape)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(COIN_ICON_SIZE),
            painter = painterResource(R.drawable.ic_coin),
            contentDescription = null,
        )
        AutoSizeText(
            modifier = Modifier.padding(start = spacingXS),
            text = collectionCardPairModel.coins.toString(),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

private val COIN_ICON_SIZE = 32.dp

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
