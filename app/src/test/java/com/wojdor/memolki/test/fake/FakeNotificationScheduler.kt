package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.notification.NotificationScheduler
import io.mockk.mockk
import javax.inject.Inject
import kotlin.random.Random

class FakeNotificationScheduler @Inject constructor(
    random: Random
) : NotificationScheduler(mockk(), random) {

    var reminderNotificationScheduled = false
        private set

    var adRewardNotificationScheduled = false
        private set

    var streakNotificationScheduled = false
        private set

    var adRewardNotificationCancelled = false
        private set

    var notificationsDismissed = false
        private set

    var channelCreated = false
        private set

    var hasPermission = false

    override fun scheduleReminderNotification() {
        reminderNotificationScheduled = true
    }

    override fun scheduleAdRewardNotification() {
        adRewardNotificationScheduled = true
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

    override fun hasNotificationPermission(): Boolean {
        return hasPermission
    }
}
