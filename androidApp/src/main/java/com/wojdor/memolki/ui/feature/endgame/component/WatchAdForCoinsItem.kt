package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.isSmallScreen
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.util.throttleClick

@Composable
fun WatchAdForCoinsItem(
    modifier: Modifier = Modifier,
    rewardedCoins: Long = 0L,
    onClick: () -> Unit = {}
) {
    EdgeSparklesEffect(
        modifier = modifier
            .pulseEffect()
            .bounceClickEffect()
    ) {
        Button(
            onClick = throttleClick(onClick = onClick),
            shape = RoundedCornerShape(spacingL),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
        ) {
            val adIconSize = if (isSmallScreen) 80.dp else 128.dp
            val coinIconSize = if (isSmallScreen) 40.dp else 64.dp
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier
                        .size(adIconSize),
                    alignment = Alignment.Center,
                    painter = painterResource(R.drawable.ic_ads),
                    contentDescription = stringResource(Res.string.watch_ad)
                )
                AutoSizeText(
                    text = stringResource(Res.string.watch_ad).lowercase(),
                    style = MaterialTheme.typography.bodyLarge.animated()
                )
            }
            if (rewardedCoins > 0) {
                Spacer(modifier = Modifier.size(spacingM))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+ $rewardedCoins",
                        style = MaterialTheme.typography.displayMedium.animated()
                    )
                    Spacer(modifier = Modifier.size(spacingM))
                    Image(
                        modifier = Modifier.size(coinIconSize),
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = stringResource(Res.string.coins)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WatchAdForCoinsItemPreview() {
    AppTheme {
        WatchAdForCoinsItem(rewardedCoins = 12)
    }
}
