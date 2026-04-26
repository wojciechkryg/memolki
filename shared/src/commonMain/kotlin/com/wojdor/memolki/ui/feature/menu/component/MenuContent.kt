package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import com.wojdor.memolki.shared.resources.ic_leaderboard
import com.wojdor.memolki.shared.resources.ic_settings
import com.wojdor.memolki.shared.resources.leaderboard
import com.wojdor.memolki.shared.resources.settings
import com.wojdor.memolki.ui.component.EdgeSparklesEffectWhen
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.feature.menu.MenuCallbacks
import com.wojdor.memolki.ui.feature.menu.MenuState
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MenuContent(
    state: MenuState,
    callbacks: MenuCallbacks
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val logoApp = state.currentApp ?: AppModel.FruitHalf
            Image(
                modifier = Modifier
                    .size(320.dp)
                    .weight(2f),
                painter = painterResource(logoApp.imageRes),
                alignment = Alignment.BottomCenter,
                contentScale = ContentScale.Fit,
                contentDescription = stringResource(Res.string.app_logo)
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
                                is MenuModel.Play -> EdgeSparklesEffectWhen(!state.hasPlayedAnyGame) {
                                    Box(
                                        modifier = if (!state.hasPlayedAnyGame) Modifier.pulseEffect() else Modifier
                                    ) {
                                        MenuItem(
                                            textId = menuItem.textId,
                                            onClick = callbacks.onPlayClick
                                        )
                                    }
                                }

                                is MenuModel.Collection -> MenuItem(
                                    textId = menuItem.textId,
                                    onClick = callbacks.onCollectionClick
                                )

                                is MenuModel.DailyReward -> DailyRewardItem(
                                    onClick = callbacks.onDailyRewardClick
                                )

                                is MenuModel.Leaderboard -> IconItem(
                                    iconRes = Res.drawable.ic_leaderboard,
                                    contentDescription = stringResource(Res.string.leaderboard),
                                    onClick = callbacks.onLeaderboardClick
                                )

                                is MenuModel.Settings -> {}
                            }
                        }
                    }
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
        val cardHeight = CARD_IMAGE_HEIGHT + spacingM * 2
        val settingsBottomPadding = if (state.otherAppModel != null && !RECORDING_MODE) {
            spacingL + cardHeight + spacingS
        } else {
            spacingL
        }
        IconItem(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = spacingL, bottom = settingsBottomPadding),
            iconRes = Res.drawable.ic_settings,
            size = 32.dp,
            contentDescription = stringResource(Res.string.settings),
            onClick = callbacks.onSettingsClick
        )
    }
}

@Preview
@Composable
private fun MenuContentPreview() {
    AppTheme {
        MenuContent(
            state = MenuState(
                menu = listOf(
                    MenuModel.Play,
                    MenuModel.Collection,
                    MenuModel.Leaderboard,
                    MenuModel.Settings
                ),
                currentApp = AppModel.FruitHalf,
                otherAppModel = AppModel.VegetableHalf
            ),
            callbacks = MenuCallbacks()
        )
    }
}

@Preview
@Composable
private fun MenuContentWithoutMoreAppsPreview() {
    AppTheme {
        MenuContent(
            state = MenuState(
                menu = listOf(
                    MenuModel.Play,
                    MenuModel.Collection,
                    MenuModel.Leaderboard,
                    MenuModel.Settings
                ),
                currentApp = AppModel.FruitHalf
            ),
            callbacks = MenuCallbacks()
        )
    }
}
