package com.wojdor.memolki.ui.feature.moreapps

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.IsAppInstalledUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.OpenApp
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsEffect.ShowAppInstall
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsIntent.OnAppClick
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MoreAppsViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val getMoreAppsUseCase: GetMoreAppsUseCase,
    private val isAppInstalledUseCase: IsAppInstalledUseCase,
) : MviViewModel<MoreAppsIntent, MoreAppsState>(
    savedStateHandle,
    MoreAppsState.serializer(),
    MoreAppsState()
) {

    init {
        loadApps()
    }

    override fun onIntent(intent: MoreAppsIntent) {
        when (intent) {
            is OnAppClick -> onAppClick(intent.app)
        }
    }

    private fun onAppClick(app: AppModel) {
        hapticFeedback.vibrateLow()
        isAppInstalledUseCase(app.appId).onEach {
            it.onSuccess { isInstalled ->
                if (isInstalled) {
                    analytics.logCrossPromotionAppOpened(app.appId)
                    sendEffect(OpenApp(app))
                } else {
                    analytics.logCrossPromotionStoreOpened(app.appId)
                    sendEffect(ShowAppInstall(app))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadApps() {
        getMoreAppsUseCase().onEach { moreAppsResult ->
            moreAppsResult.onSuccess {
                sendState {
                    copy(
                        apps = it
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
