package com.wojdor.memolki.ui.feature.enablenotifications

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.app.AppNavigation
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.analytics.Analytics

class EnableNotificationsViewModel(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics
) : MviViewModel<EnableNotificationsIntent, EnableNotificationsState>(
    savedStateHandle,
    EnableNotificationsState.serializer(),
    EnableNotificationsState()
) {

    private val destination = EnableNotificationDestination.fromRoute(
        savedStateHandle.get<String>(AppNavigation.DESTINATION_ARG).orEmpty()
    )
    private val boardId = savedStateHandle.get<String>(AppNavigation.BOARD_ARG).orEmpty()

    override fun onIntent(intent: EnableNotificationsIntent) {
        when (intent) {
            EnableNotificationsIntent.OnEnableClick -> onEnableClick()
            EnableNotificationsIntent.OnLaterClick -> {
                analytics.logNotificationEnabled(false)
                navigateToDestination()
            }

            is EnableNotificationsIntent.OnPermissionResult -> {
                analytics.logNotificationEnabled(intent.isGranted)
                navigateToDestination()
            }
        }
    }

    private fun onEnableClick() {
        sendEffect(EnableNotificationsEffect.RequestNotificationPermission)
    }

    private fun navigateToDestination() {
        val effect = when (destination) {
            EnableNotificationDestination.GAME -> EnableNotificationsEffect.NavigateToGame(boardId)
            EnableNotificationDestination.MENU -> EnableNotificationsEffect.NavigateToMenu
            EnableNotificationDestination.COLLECTION -> EnableNotificationsEffect.NavigateToCollection
            EnableNotificationDestination.SHOP -> EnableNotificationsEffect.NavigateToShop
        }
        sendEffect(effect)
    }
}
