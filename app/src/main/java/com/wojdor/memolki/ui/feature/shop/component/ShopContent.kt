package com.wojdor.memolki.ui.feature.shop.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.feature.shop.ShopCallbacks
import com.wojdor.memolki.ui.feature.shop.ShopState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ShopContent(
    state: ShopState,
    callbacks: ShopCallbacks = ShopCallbacks()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinsAmount(
                modifier = Modifier.weight(1f),
                coins = state.coins,
                animate = state.animateCoins
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.shop))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CollectionPreview() {
    AppTheme {
        ShopContent(
            state = ShopState(1234)
        )
    }
}
