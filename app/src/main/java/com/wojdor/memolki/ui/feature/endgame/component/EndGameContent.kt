package com.wojdor.memolki.ui.feature.endgame.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.EndGameMenuModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.ui.components.CoinsAmount
import com.wojdor.memolki.ui.feature.endgame.EndGameCallbacks
import com.wojdor.memolki.ui.feature.endgame.EndGameState
import com.wojdor.memolki.ui.feature.menu.component.MenuItem
import com.wojdor.memolki.ui.theme.AppTheme

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
            modifier = Modifier.padding(horizontal = 16.dp),
            coins = state.currentCoins,
            animate = state.animateCoins
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoinsReward(state = state)
            Spacer(modifier = Modifier.height(64.dp))
            state.menu.forEach { menuItem ->
                Spacer(modifier = Modifier.height(16.dp))
                when (menuItem) {
                    is EndGameMenuModel.PlayAgain -> MenuItem(
                        textId = menuItem.textId,
                        onClick = callbacks.onPlayAgainClick
                    )

                    is EndGameMenuModel.Menu -> MenuItem(
                        textId = menuItem.textId,
                        onClick = callbacks.onMenuClick
                    )
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
                menu = listOf(EndGameMenuModel.PlayAgain, EndGameMenuModel.Menu)
            )
        )
    }
}
