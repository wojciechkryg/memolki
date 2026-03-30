package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.component.RECORDING_MODE
import com.wojdor.memolki.ui.component.XmlDrawable
import com.wojdor.memolki.ui.feature.menu.MenuCallbacks
import com.wojdor.memolki.ui.feature.menu.MenuState
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL

@Composable
fun MenuContent(
    state: MenuState,
    callbacks: MenuCallbacks
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        XmlDrawable(
            modifier = Modifier
                .size(320.dp)
                .weight(2f),
            drawableRes = R.drawable.ic_logo,
            alignment = Alignment.BottomCenter,
            contentDescription = stringResource(R.string.app_logo)
        )
        Spacer(modifier = Modifier.weight(0.4f))
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
                    verticalArrangement = Arrangement.spacedBy(spacingL),
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
            Spacer(modifier = Modifier.height(spacingL))
            Row {
                IconItem(
                    iconRes = R.drawable.ic_leaderboard,
                    contentDescription = stringResource(R.string.leaderboard),
                    onClick = callbacks.onLeaderboardClick
                )
            }
        }
        if (!RECORDING_MODE) {
            state.otherAppModel?.let {
                MoreAppsItem(
                    modifier = Modifier.fillMaxWidth(),
                    appModel = it,
                    onClick = callbacks.onMoreAppsClick
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
                ),
                AppModel.VegetableHalf
            ),
            callbacks = MenuCallbacks()
        )
    }
}
