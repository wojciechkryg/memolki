package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.component.SparklesOverlay
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.feature.endgame.EndGameCallbacks
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.feature.menu.component.MenuItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import com.wojdor.memolki.util.throttleClick

@Composable
fun CasualEndGameContent(
    state: EndGameState,
    callbacks: EndGameCallbacks
) {
    LaunchedEffect(state.showSparkles) {
        if (state.showSparkles) {
            delay(LEVEL_COMPLETE_DELAY)
            callbacks.onLevelComplete()
        }
    }
    LaunchedEffect(state.rewardedCoins) {
        if (state.rewardedCoins > 0) {
            delay(REWARD_COINS_DELAY)
            callbacks.onRewardCoinsReady()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
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
                Spacer(modifier = Modifier.weight(1f))
                CoinsReward(
                    rewardedCoins = state.rewardedCoins,
                    animate = state.animateRewardCoins
                )
                Spacer(modifier = Modifier.weight(1f))
                AnimatedContent(
                    state.menu,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spacingL),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        it.filterNot { item ->
                            @Suppress("KotlinConstantConditions")
                            RECORDING_MODE && item is EndGameMenuModel.WatchAd
                                    || RECORDING_MODE && item is EndGameMenuModel.FreeCoins
                                    || RECORDING_MODE && item is EndGameMenuModel.Share
                        }.forEach { menuItem ->
                            when (menuItem) {
                                EndGameMenuModel.WatchAd ->
                                    WatchAdMultiplyRewardItem(onClick = callbacks.onWatchAdClick)

                                EndGameMenuModel.UnlockNewCard -> UnlockNewCardItem(
                                    onClick = callbacks.onUnlockNewCardClick
                                )

                                EndGameMenuModel.FreeCoins -> FreeCoinsItem(
                                    onClick = callbacks.onFreeCoinsClick
                                )

                                EndGameMenuModel.PlayAgain -> MenuItem(
                                    textId = menuItem.textId,
                                    isUppercase = false,
                                    onClick = callbacks.onPlayAgainClick
                                )

                                EndGameMenuModel.Menu -> MenuItem(
                                    textId = menuItem.textId,
                                    isUppercase = false,
                                    onClick = callbacks.onMenuClick
                                )

                                is EndGameMenuModel.Share -> ShareButton(
                                    shareModel = menuItem,
                                    onClick = callbacks.onShareClick
                                )

                                else -> {}
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }
        SparklesOverlay(isActive = state.showSparkles)
    }
}

@Composable
private fun ShareButton(
    shareModel: EndGameMenuModel.Share,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            modifier = Modifier.bounceClickEffect(),
            onClick = throttleClick(onClick),
            contentPadding = PaddingValues(spacingL),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.share)
            )
        }
        val rewardAlpha by animateFloatAsState(
            targetValue = if (shareModel.showReward) 1f else 0f,
            label = "share reward alpha"
        )
        if (rewardAlpha > 0f) {
            ShareRewardLabel(
                modifier = Modifier
                    .padding(start = spacingS)
                    .alpha(rewardAlpha),
                coins = shareModel.rewardCoins
            )
        }
    }
}

@Composable
private fun ShareRewardLabel(
    modifier: Modifier = Modifier,
    coins: Long = 0L
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingS)
    ) {
        Text(
            text = "+$coins",
            style = MaterialTheme.typography.headlineSmall
        )
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_coin),
            contentDescription = stringResource(R.string.coins)
        )
    }
}

@Preview
@Composable
private fun CasualEndGameContentPreview() {
    AppTheme {
        CasualEndGameContent(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.WatchAd,
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.UnlockNewCard,
                    EndGameMenuModel.Share(showReward = true, rewardCoins = 3)
                )
            ),
            callbacks = EndGameCallbacks()
        )
    }
}

@Preview
@Composable
private fun CasualEndGameContentWithoutAdPreview() {
    AppTheme {
        CasualEndGameContent(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.Share(showReward = false)
                )
            ),
            callbacks = EndGameCallbacks()
        )
    }
}

@Preview
@Composable
private fun CasualEndGameContentWithFreeCoinsPreview() {
    AppTheme {
        CasualEndGameContent(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.WatchAd,
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.FreeCoins,
                    EndGameMenuModel.Share(showReward = true, rewardCoins = 3)
                )
            ),
            callbacks = EndGameCallbacks()
        )
    }
}

private const val LEVEL_COMPLETE_DELAY = 250L
private const val REWARD_COINS_DELAY = 500L
