package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.component.CoinsAmount
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
                state = state,
                animate = state.animateRewardCoins
            )
            Spacer(modifier = Modifier.weight(0.05f))
            AnimatedContent(
                state.menu,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    it.forEach { menuItem ->
                        Spacer(modifier = Modifier.height(spacingL))
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
        }
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
