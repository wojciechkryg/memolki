package com.wojdor.memolki.ui.feature.dailychallengehistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.FadeEffectBottom
import com.wojdor.memolki.ui.component.FadeEffectTop
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.dailychallengehistory.component.DailyChallengeHistoryItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.extension.TextSharer
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DailyChallengeHistoryScreen(
    viewModel: DailyChallengeHistoryViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: DailyChallengeHistoryViewModel,
    navController: NavController
) {
    val textSharer = koinInject<TextSharer>()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is DailyChallengeHistoryEffect.ShareDailyChallenge -> textSharer.share(effect.text)
        }
    }
}

@Composable
private fun HandleState(
    viewModel: DailyChallengeHistoryViewModel,
    state: DailyChallengeHistoryState
) {
    val callbacks = DailyChallengeHistoryCallbacks(
        onShareClick = { viewModel.sendIntent(DailyChallengeHistoryIntent.OnShareClick(it)) }
    )
    DailyChallengeHistoryScreen(state, callbacks)
}

@Composable
private fun DailyChallengeHistoryScreen(
    state: DailyChallengeHistoryState,
    callbacks: DailyChallengeHistoryCallbacks = DailyChallengeHistoryCallbacks()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = spacingL, vertical = spacingL),
            text = stringResource(Res.string.daily_challenge_history).lowercase(),
            style = MaterialTheme.typography.displaySmall
        )
        HistoryList(state, callbacks)
    }
}

@Composable
private fun HistoryList(
    state: DailyChallengeHistoryState,
    callbacks: DailyChallengeHistoryCallbacks
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacingL),
            contentPadding = PaddingValues(vertical = spacingS),
            verticalArrangement = Arrangement.spacedBy(spacingS)
        ) {
            items(
                items = state.challenges,
                key = { it.epochDay }
            ) { challenge ->
                DailyChallengeHistoryItem(
                    challenge = challenge,
                    isToday = challenge.epochDay == state.todayEpochDay,
                    onShareClick = { callbacks.onShareClick(challenge) }
                )
            }
        }
        FadeEffectTop(Modifier.align(Alignment.TopCenter))
        FadeEffectBottom(Modifier.align(Alignment.BottomCenter))
    }
}

@Preview
@Composable
private fun DailyChallengeHistoryScreenPreview() {
    val todayEpochDay = LocalDate(2026, 4, 11).toEpochDays().toLong()
    AppTheme {
        PreviewBackground {
            DailyChallengeHistoryScreen(
            state = DailyChallengeHistoryState(
                todayEpochDay = todayEpochDay,
                challenges = listOf(
                    DailyChallengeModel(
                        epochDay = todayEpochDay,
                        mistakeCount = 0,
                        starCount = 3,
                        timeMillis = 83456L,
                        cardFlipCounts = listOf(
                            listOf(2, 2, 2, 2),
                            listOf(2, 2, 2, 2),
                            listOf(2, 2, 2, 2),
                            listOf(2, 2, 2, 2),
                            listOf(2, 2, 2, 2),
                            listOf(2, 2, 2, 2)
                        )
                    ),
                    DailyChallengeModel(
                        epochDay = todayEpochDay - 1,
                        mistakeCount = 3,
                        starCount = 2,
                        timeMillis = 152789L,
                        cardFlipCounts = listOf(
                            listOf(2, 3, 2, 4),
                            listOf(2, 2, 5, 2),
                            listOf(3, 2, 2, 2)
                        )
                    ),
                    DailyChallengeModel(
                        epochDay = todayEpochDay - 2,
                        mistakeCount = 7,
                        starCount = 1,
                        timeMillis = 245123L,
                        cardFlipCounts = listOf(
                            listOf(4, 3, 5, 2),
                            listOf(3, 6, 2, 4),
                            listOf(2, 3, 4, 5)
                        )
                    )
                )
            )
            )
        }
    }
}
