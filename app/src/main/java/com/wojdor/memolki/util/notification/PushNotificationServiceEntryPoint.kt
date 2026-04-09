package com.wojdor.memolki.util.notification

import com.wojdor.memolki.data.repository.NotificationRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PushNotificationServiceEntryPoint {
    fun notificationCreator(): NotificationCreator
    fun notificationRepository(): NotificationRepository
}
