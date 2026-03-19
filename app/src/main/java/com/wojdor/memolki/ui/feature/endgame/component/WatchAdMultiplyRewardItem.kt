package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wojdor.memolki.ui.theme.animated
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.ui.theme.spacingXS
import com.wojdor.memolki.util.throttleClick

@Composable
fun WatchAdMultiplyRewardItem(onClick: () -> Unit = {}) {
    Button(
        modifier = Modifier.pulseEffect().bounceClickEffect(),
        onClick = throttleClick(onClick = onClick),
        shape = RoundedCornerShape(spacingL),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
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
                style = MaterialTheme.typography.headlineSmall.animated()
            )
        }
        Spacer(modifier = Modifier.size(spacingM))
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(spacingXS))
        Text(
            text = stringResource(id = R.string.end_game_watch_ad_reward).lowercase(),
            style = MaterialTheme.typography.displayMedium.animated()
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
