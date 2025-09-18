package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoinsReward(
    modifier: Modifier = Modifier,
    state: EndGameState
) {
    val animatedRewardedCoins = remember { Animatable(0f) }
    LaunchedEffect(state.rewardedCoins) {
        launch {
            delay(ANIMATION_DELAY)
            animatedRewardedCoins.animateTo(
                targetValue = state.rewardedCoins.toFloat(),
                animationSpec = tween(
                    durationMillis = (state.rewardedCoins.toInt() * ANIMATION_DURATION_FACTOR)
                        .coerceIn(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION),
                    easing = FastOutSlowInEasing
                )
            )
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "+ ${animatedRewardedCoins.value.toLong()}",
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

private const val ANIMATION_DELAY = 300L
private const val ANIMATION_DURATION_FACTOR = 50
private const val MIN_ANIMATION_DURATION = 500
private const val MAX_ANIMATION_DURATION = 1000

@Preview
@Composable
private fun CoinsRewardPreview() {
    AppTheme {
        CoinsReward(state = EndGameState(level = LevelModel.Grid2x3(), rewardedCoins = 1234))
    }
}
