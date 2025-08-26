package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun CoinsReward(
    modifier: Modifier = Modifier,
    state: EndGameState
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "+ ${state.rewardedCoins}",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.size(16.dp))
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun CoinsRewardPreview() {
    AppTheme {
        CoinsReward(state = EndGameState(level = LevelModel.Grid2x3, rewardedCoins = 1234))
    }
}
