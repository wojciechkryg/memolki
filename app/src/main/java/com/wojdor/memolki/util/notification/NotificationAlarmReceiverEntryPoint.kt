package com.wojdor.memolki.util.notification

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.random.Random

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationAlarmReceiverEntryPoint {
    fun notificationScheduler(): NotificationScheduler
    fun random(): Random
}
