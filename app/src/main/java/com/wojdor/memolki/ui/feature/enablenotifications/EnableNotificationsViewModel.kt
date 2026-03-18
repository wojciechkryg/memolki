package com.wojdor.memolki.ui.feature.enablenotifications

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.app.AppNavigation
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnableNotificationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : MviViewModel<EnableNotificationsIntent, EnableNotificationsState>(
    savedStateHandle,
    EnableNotificationsState()
) {

    private val destination = EnableNotificationDestination.fromRoute(
        savedStateHandle.get<String>(AppNavigation.DESTINATION_ARG).orEmpty()
    )

    override fun onIntent(intent: EnableNotificationsIntent) {
        when (intent) {
            EnableNotificationsIntent.OnEnableClick -> onEnableClick()
            EnableNotificationsIntent.OnLaterClick -> navigateToDestination()
            EnableNotificationsIntent.OnPermissionResult -> navigateToDestination()
        }
    }

    private fun onEnableClick() {
        sendEffect(EnableNotificationsEffect.RequestNotificationPermission)
    }

    private fun navigateToDestination() {
        val effect = when (destination) {
            EnableNotificationDestination.GAME -> EnableNotificationsEffect.NavigateToGame
            EnableNotificationDestination.MENU -> EnableNotificationsEffect.NavigateToMenu
            EnableNotificationDestination.COLLECTION -> EnableNotificationsEffect.NavigateToCollection
        }
        sendEffect(effect)
    }
}
