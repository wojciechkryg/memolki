package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.mockk
import javax.inject.Inject
import kotlin.random.Random

class FakeNotificationScheduler @Inject constructor(
    random: Random,
    permissionProvider: PermissionProvider
) : NotificationScheduler(mockk(), random, mockk(), permissionProvider) {

    var reminderNotificationScheduled = false
        private set

    var adRewardNotificationScheduled = false
        private set

    var dailyChallengeNotificationScheduled = false
        private set

    var streakNotificationScheduled = false
        private set

    var adRewardNotificationCancelled = false
        private set

    var notificationsDismissed = false
        private set

    var channelCreated = false
        private set

    override fun scheduleReminderNotification() {
        reminderNotificationScheduled = true
    }

    override fun scheduleAdRewardNotification() {
        adRewardNotificationScheduled = true
    }

    override fun scheduleDailyChallengeNotification() {
        dailyChallengeNotificationScheduled = true
    }

    override fun scheduleStreakNotification() {
        streakNotificationScheduled = true
    }

    override fun cancelAdRewardNotification() {
        adRewardNotificationCancelled = true
    }

    override fun dismissVisibleNotifications() {
        notificationsDismissed = true
    }

    override fun createNotificationChannel() {
        channelCreated = true
    }

}
