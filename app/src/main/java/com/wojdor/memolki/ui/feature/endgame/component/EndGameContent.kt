package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.component.CoinsAmount
import com.wojdor.memolki.ui.component.SparklesOverlay
import com.wojdor.memolki.ui.feature.endgame.EndGameCallbacks
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.feature.menu.component.MenuItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL

@Composable
fun EndGameContent(
    state: EndGameState,
    callbacks: EndGameCallbacks = EndGameCallbacks()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
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
                Spacer(modifier = Modifier.weight(0.15f))
                CoinsReward(
                    rewardedCoins = state.rewardedCoins,
                    animate = state.animateRewardCoins
                )
                Spacer(modifier = Modifier.weight(0.1f))
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
                        it.forEach { menuItem ->
                            when (menuItem) {
                                EndGameMenuModel.WatchAd ->
                                    WatchAdMultiplyRewardItem(onClick = callbacks.onWatchAdClick)

                                EndGameMenuModel.UnlockNewCard -> UnlockNewCardItem(
                                    onClick = callbacks.onUnlockNewCardClick
                                )

                                EndGameMenuModel.PlayAgain -> MenuItem(
                                    textId = menuItem.textId,
                                    onClick = callbacks.onPlayAgainClick
                                )

                                EndGameMenuModel.Menu -> MenuItem(
                                    textId = menuItem.textId,
                                    onClick = callbacks.onMenuClick
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
        SparklesOverlay(isActive = state.showSparkles)
    }
}

@Composable
@Preview
private fun EndGameContentPreview() {
    AppTheme {
        EndGameContent(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.WatchAd,
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu,
                    EndGameMenuModel.UnlockNewCard
                )
            )
        )
    }
}

@Composable
@Preview
private fun EndGameContentWithoutAdPreview() {
    AppTheme {
        EndGameContent(
            state = EndGameState(
                level = LevelModel.Grid2x3(),
                rewardedCoins = 1234,
                currentCoins = 5678,
                menu = listOf(
                    EndGameMenuModel.PlayAgain,
                    EndGameMenuModel.Menu
                )
            )
        )
    }
}
