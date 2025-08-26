package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.feature.collection.CollectionState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CoinsAmount(
    modifier: Modifier = Modifier,
    state: CollectionState
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            modifier = modifier,
            text = state.coins.toString(),
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Preview
@Composable
private fun CoinsRewardPreview() {
    AppTheme {
        CoinsAmount(state = CollectionState(coins = 1234))
    }
}
