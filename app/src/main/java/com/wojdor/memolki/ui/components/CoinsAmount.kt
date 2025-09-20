package com.wojdor.memolki.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun CoinsAmount(
    modifier: Modifier = Modifier,
    coins: Long,
    animate: Boolean = false
) {
    val animatedAmount = remember { Animatable(0f) }

    LaunchedEffect(coins) {
        if (animate) {
            launch {
                animatedAmount.animateTo(
                    targetValue = coins.toFloat(),
                    animationSpec = tween(
                        durationMillis = ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        } else {
            animatedAmount.snapTo(coins.toFloat())
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = animatedAmount.value.toLong().toString(),
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private const val ANIMATION_DURATION = 1500

@Preview
@Composable
private fun CoinsRewardPreview() {
    AppTheme {
        CoinsAmount(
            modifier = Modifier.width(128.dp),
            coins = 1234L,
            animate = false
        )
    }
}
