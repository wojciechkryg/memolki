package com.wojdor.memolki.util.notification

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PushNotificationServiceEntryPoint {
    fun notificationCreator(): NotificationCreator
}
