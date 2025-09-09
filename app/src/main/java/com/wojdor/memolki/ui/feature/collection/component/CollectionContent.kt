package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.ui.feature.collection.CollectionCallbacks
import com.wojdor.memolki.ui.feature.collection.CollectionState
import com.wojdor.memolki.ui.feature.collection.getCollectionStateForPreview
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CollectionContent(
    state: CollectionState,
    callbacks: CollectionCallbacks = CollectionCallbacks()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoinsAmount(
                modifier = Modifier.weight(1f),
                state = state
            )
            ShopButton(
                onClick = callbacks.onShopButtonClick
            )
        }
        CardPairsCollection(state)
    }
}

@Composable
@Preview(showBackground = true)
private fun CollectionPreview() {
    AppTheme {
        CollectionContent(
            state = getCollectionStateForPreview()
        )
    }
}
