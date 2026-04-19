package com.wojdor.memolki.ui.feature.enablenotifications

data class EnableNotificationsCallbacks(
    val onEnableClick: () -> Unit = {},
    val onLaterClick: () -> Unit = {}
)
