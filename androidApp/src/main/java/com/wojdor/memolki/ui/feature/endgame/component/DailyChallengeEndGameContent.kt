package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.component.CompareButton
import com.wojdor.memolki.ui.component.SparklesOverlay
import com.wojdor.memolki.ui.component.TimeDisplay
import com.wojdor.memolki.ui.feature.endgame.EndGameCallbacks
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.isSmallScreen
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.delay

@Composable
fun DailyChallengeEndGameContent(
    state: EndGameState,
    callbacks: EndGameCallbacks
) {
    val starCount = state.dailyChallenge.starCount
    var showSparkles by remember { mutableStateOf(false) }
    LaunchedEffect(state.showSparkles) {
        if (state.showSparkles) {
            delay(LEVEL_COMPLETE_DELAY)
            callbacks.onLevelComplete()
            if (starCount > 0) {
                val lastStarDelay = STAR_INITIAL_DELAY +
                        STAR_DELAY * (starCount - 1) +
                        STAR_SPRING_DURATION
                delay(lastStarDelay - LEVEL_COMPLETE_DELAY)
                showSparkles = true
            }
        }
    }
    LaunchedEffect(state.rewardedCoins) {
        if (state.rewardedCoins > 0) {
            delay(REWARD_COINS_DELAY)
            callbacks.onRewardCoinsReady()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        DailyChallengeContent(state, callbacks)
        SparklesOverlay(isActive = showSparkles)
    }
}

@Composable
private fun DailyChallengeContent(
    state: EndGameState,
    callbacks: EndGameCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CoinsAmount(
            modifier = Modifier.padding(horizontal = spacingL),
            coins = state.currentCoins,
            animate = state.animateCoins
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isPreview = LocalInspectionMode.current
            val starCount = state.dailyChallenge.starCount
            val hasRewards = state.rewardedCoins > 0
            val lastStarFinished =
                STAR_INITIAL_DELAY + STAR_DELAY * (starCount - 1).coerceAtLeast(0) + STAR_SPRING_DURATION

            var starsFinished by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(lastStarFinished)
                starsFinished = true
                callbacks.onDailyChallengeStarsAnimationFinished()
            }
            val showDetails = starsFinished || isPreview
            val showReward = hasRewards && showDetails
            val detailsAlpha by animateFloatAsState(
                targetValue = if (showDetails) 1f else 0f,
                animationSpec = tween(durationMillis = FADE_IN_DURATION),
                label = "details_alpha"
            )
            val contentAlpha by animateFloatAsState(
                targetValue = if (showReward) 1f else 0f,
                animationSpec = tween(durationMillis = FADE_IN_DURATION),
                label = "content_alpha"
            )
            var showMenu by remember { mutableStateOf(isPreview) }
            val menuAlpha by animateFloatAsState(
                targetValue = if (showMenu) 1f else 0f,
                animationSpec = tween(durationMillis = FADE_IN_DURATION),
                label = "menu_alpha"
            )
            LaunchedEffect(showDetails) {
                if (showDetails) {
                    delay(STAR_DELAY)
                    showMenu = true
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Stars(state.dailyChallenge.starCount)
            Spacer(modifier = Modifier.padding(spacingS))
            MistakeCount(
                modifier = Modifier.alpha(detailsAlpha),
                mistakeCount = state.dailyChallenge.mistakeCount
            )
            Spacer(modifier = Modifier.padding(spacingS))
            TimeDisplay(
                modifier = Modifier.alpha(detailsAlpha),
                timeMillis = state.dailyChallenge.timeMillis
            )

            Spacer(modifier = Modifier.padding(spacingL))
            CoinsReward(
                modifier = Modifier.alpha(contentAlpha),
                rewardedCoins = state.rewardedCoins,
                animate = state.animateRewardCoins
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.alpha(menuAlpha),
                verticalArrangement = Arrangement.spacedBy(spacingL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.menu.forEach { menuItem ->
                    if (RECORDING_MODE && menuItem is EndGameMenuModel.WatchAd) return@forEach
                    when (menuItem) {
                        EndGameMenuModel.WatchAd -> WatchAdForCoinsItem(
                            rewardedCoins = state.rewardedCoins,
                            onClick = callbacks.onWatchAdClick
                        )

                        EndGameMenuModel.Compare -> CompareButton(onClick = callbacks.onDailyChallengeShareClick)
                        EndGameMenuModel.Menu -> BaseMenuItem(
                            textId = menuItem.textId,
                            onClick = callbacks.onMenuClick
                        )

                        else -> {}
                    }
                }
            }
            Spacer(modifier = Modifier.weight(2f))
        }
    }
}

@Composable
private fun Stars(starCount: Int) {
    val starSize = if (isSmallScreen) 64.dp else 96.dp
    val isPreview = LocalInspectionMode.current
    Row {
        repeat(starCount.coerceIn(0, 3)) { index ->
            var visible by remember { mutableStateOf(isPreview) }
            LaunchedEffect(Unit) {
                delay(STAR_INITIAL_DELAY + STAR_DELAY * index)
                visible = true
            }
            val scale by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "star_scale"
            )
            val rotation by animateFloatAsState(
                targetValue = if (visible) 0f else -180f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "star_rotation"
            )
            Image(
                modifier = Modifier
                    .size(starSize)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    },
                painter = painterResource(Res.drawable.ic_star),
                contentDescription = null
            )
        }
    }
}

private const val STAR_INITIAL_DELAY = 300L
private const val STAR_DELAY = 500L
private const val STAR_SPRING_DURATION = 300L
private const val FADE_IN_DURATION = 500
private const val LEVEL_COMPLETE_DELAY = 250L
private const val REWARD_COINS_DELAY = 500L

@Composable
private fun MistakeCount(modifier: Modifier = Modifier, mistakeCount: Int) {
    Text(
        modifier = modifier,
        text = pluralStringResource(Res.plurals.daily_challenge_mistakes, mistakeCount, mistakeCount),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview
@Composable
private fun DailyChallengeEndGameContentPreview() {
    AppTheme {
        DailyChallengeEndGameContent(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 0,
                    starCount = 3,
                    timeMillis = 83456L,
                    epochDay = 42L
                ),
                rewardedCoins = 1234,
                currentCoins = 5678
            ),
            callbacks = EndGameCallbacks()
        )
    }
}

@Preview
@Composable
private fun DailyChallengeEndGameContentTwoStarsPreview() {
    AppTheme {
        DailyChallengeEndGameContent(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 3,
                    starCount = 2,
                    timeMillis = 152789L,
                    epochDay = 15L
                ),
                rewardedCoins = 890,
                currentCoins = 3456
            ),
            callbacks = EndGameCallbacks()
        )
    }
}

@Preview
@Composable
private fun DailyChallengeEndGameContentOneStarPreview() {
    AppTheme {
        DailyChallengeEndGameContent(
            state = EndGameState(
                dailyChallenge = DailyChallengeModel(
                    mistakeCount = 5,
                    starCount = 1,
                    timeMillis = 245123L,
                    epochDay = 7L
                ),
                rewardedCoins = 567,
                currentCoins = 1234
            ),
            callbacks = EndGameCallbacks()
        )
    }
}
