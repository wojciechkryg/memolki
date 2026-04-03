package com.wojdor.memolki.util.notification

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.TimeProvider
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
    fun dailyChallengeRepository(): DailyChallengeRepository
    fun timeProvider(): TimeProvider
}
