package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.throttleClick

@Composable
fun WatchAdMultiplyRewardItem(onClick: () -> Unit = {}) {
    Button(
        onClick = throttleClick(onClick = onClick),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent
        ),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .size(128.dp),
                alignment = Alignment.Center,
                painter = painterResource(R.drawable.ic_ads),
                contentDescription = null
            )
            AutoSizeText(
                text = stringResource(R.string.watch_ad).uppercase(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(id = R.string.end_game_watch_ad_reward).lowercase(),
            style = MaterialTheme.typography.displayMedium
        )

    }
}

@Preview
@Composable
private fun WatchAdMultiplyRewardItemPreview() {
    AppTheme {
        WatchAdMultiplyRewardItem()
    }
}
