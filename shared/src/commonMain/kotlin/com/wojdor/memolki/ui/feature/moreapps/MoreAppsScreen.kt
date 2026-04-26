package com.wojdor.memolki.ui.feature.moreapps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.moreapps.component.MoreAppsContent
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.AppOpener
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoreAppsScreen(
    viewModel: MoreAppsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: MoreAppsViewModel
) {
    val appOpener = koinInject<AppOpener>()
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is MoreAppsEffect.ShowAppInstall -> appOpener.showAppInstall(effect.app.appId)
            is MoreAppsEffect.OpenApp -> appOpener.openApp(effect.app.appId)
        }
    }
}

@Composable
private fun HandleState(
    viewModel: MoreAppsViewModel,
    state: MoreAppsState
) {
    val callbacks = MoreAppsCallbacks(
        onAppClick = { viewModel.sendIntent(MoreAppsIntent.OnAppClick(it)) },
    )
    MoreAppsScreen(state, callbacks)
}

@Composable
private fun MoreAppsScreen(
    state: MoreAppsState,
    callbacks: MoreAppsCallbacks
) {
    MoreAppsContent(state, callbacks)
}

@Preview
@Composable
private fun MoreAppsScreenPreview() {
    AppTheme {
        PreviewBackground {
            MoreAppsScreen(
                state = MoreAppsState(AppModel.all()),
                callbacks = MoreAppsCallbacks()
            )
        }
    }
}
