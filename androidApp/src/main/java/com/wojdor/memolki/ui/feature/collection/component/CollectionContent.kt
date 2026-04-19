package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.feature.collection.CollectionCallbacks
import com.wojdor.memolki.ui.feature.collection.CollectionState
import com.wojdor.memolki.ui.feature.collection.getCollectionStateForPreview
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS

@Composable
fun CollectionContent(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(horizontal = spacingL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinsAmount(
                modifier = Modifier.weight(1f),
                coins = state.coins,
                animate = state.animateCoins
            )
            ShopButton(
                onClick = callbacks.onShopButtonClick
            )
        }
        UnlockedCardPairsCounter(
            modifier = Modifier.padding(bottom = spacingS),
            state = state
        )
        CardPairsCollection(state, callbacks)
    }
}

@Preview
@Composable
private fun CollectionPreview() {
    AppTheme {
        CollectionContent(
            state = getCollectionStateForPreview()
        )
    }
}
