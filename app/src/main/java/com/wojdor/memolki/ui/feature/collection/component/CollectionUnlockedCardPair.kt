package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.feature.collection.getCollectionStateForPreview
import com.wojdor.memolki.ui.feature.game.component.FrontCardItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.isTablet
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.util.throttleClick

@Composable
fun CollectionUnlockedCardPair(
    modifier: Modifier = Modifier,
    collectionCardPairModel: CollectionCardPairModel.Unlocked,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isTablet) spacingL else 0.dp)
            .bounceClickEffect()
            .clip(rotatedCardPairShape)
            .clickable(
                onClickLabel = stringResource(R.string.accessibility_view_card_pair),
                onClick = throttleClick(onClick = onClick)
            )
            .padding(vertical = CARD_PAIR_VERTICAL_PADDING),
        contentAlignment = Alignment.Center
    ) {
        FrontCardItem(
            modifier = cardLeftModifier,
            card = collectionCardPairModel.cardPair.second
        )
        FrontCardItem(
            modifier = cardRightModifier,
            card = collectionCardPairModel.cardPair.first
        )
    }
}

@Preview
@Composable
fun CollectionUnlockedCardPairPreview() {
    AppTheme {
        CollectionUnlockedCardPair(
            modifier = Modifier.width(192.dp),
            collectionCardPairModel = getCollectionStateForPreview()
                .collectionCardPairs
                .first { it is CollectionCardPairModel.Unlocked } as CollectionCardPairModel.Unlocked
        )
    }
}
