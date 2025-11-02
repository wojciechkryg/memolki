package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.feature.menu.MenuCallbacks
import com.wojdor.memolki.ui.feature.menu.MenuState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun MenuContent(
    state: MenuState,
    callbacks: MenuCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(320.dp)
                .weight(2f),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            alignment = Alignment.BottomCenter
        )
        Spacer(modifier = Modifier.height(64.dp))
        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = state.menu,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    it.forEach { menuItem ->
                        when (menuItem) {
                            is MenuModel.NewGame -> MenuItem(
                                textId = menuItem.textId,
                                onClick = callbacks.onNewGameClick
                            )

                            is MenuModel.Collection -> MenuItem(
                                textId = menuItem.textId,
                                onClick = callbacks.onCollectionClick
                            )

                            is MenuModel.Settings -> MenuItem(
                                textId = R.string.settings,
                                onClick = callbacks.onSettingsClick
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                IconItem(
                    iconRes = R.drawable.ic_leaderboard,
                    onClick = callbacks.onLeaderboardClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun MenuContentPreview() {
    AppTheme {
        MenuContent(
            state = MenuState(
                listOf(
                    MenuModel.NewGame,
                    MenuModel.Collection,
                    MenuModel.Settings
                )
            ),
            callbacks = MenuCallbacks()
        )
    }
}
