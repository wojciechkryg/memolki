package com.wojdor.memolki.ui.feature.moreapps

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.feature.moreapps.component.MoreAppsContent
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun MoreAppsScreen(
    viewModel: MoreAppsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: MoreAppsViewModel
) {
    val activity = LocalActivity.current
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is MoreAppsEffect.ShowAppInstall -> activity?.let {
                showAppInstall(it, effect.app)
            }

            is MoreAppsEffect.OpenApp -> activity?.let {
                openApp(it, effect.app)
            }
        }
    }
}

private fun showAppInstall(
    activity: Activity,
    app: AppModel
) {
    TODO()
}

private fun openApp(
    activity: Activity,
    app: AppModel
) {
    TODO()
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

@Preview(showBackground = true)
@Composable
private fun MoreAppsScreenPreview() {
    AppTheme {
        MoreAppsScreen(
            state = MoreAppsState(AppModel.all()),
            callbacks = MoreAppsCallbacks()
        )
    }
}
