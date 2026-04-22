package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import kotlin.math.roundToLong

@Composable
fun CoinsReward(
    modifier: Modifier = Modifier,
    rewardedCoins: Long,
    animate: Boolean = false
) {
    val animatedAmount by animateFloatAsState(
        targetValue = rewardedCoins.toFloat(),
        animationSpec = if (animate) {
            tween(
                durationMillis = ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        } else {
            snap()
        },
        label = "coins reward animation"
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "+ ${animatedAmount.roundToLong()}",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.size(spacingL))
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(Res.drawable.ic_coin),
            contentDescription = stringResource(Res.string.coins),
        )
    }
}

private const val ANIMATION_DURATION = 1000

@Preview
@Composable
private fun CoinsRewardPreview() {
    AppTheme {
        CoinsReward(rewardedCoins = 1234)
    }
}
