package com.wojdor.memolki.util.notification

import com.wojdor.memolki.util.provider.LocaleProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.random.Random

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationAlarmReceiverEntryPoint {
    fun notificationScheduler(): NotificationScheduler
    fun notificationCreator(): NotificationCreator
    fun localeProvider(): LocaleProvider
    fun random(): Random
}
