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

    var dailyChallengeNotificationTimestamp: Long? = null
        private set

    var nextDailyChallengeNotificationTimestamp: Long = FAKE_NEXT_DAILY_CHALLENGE_NOTIFICATION_TIMESTAMP
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

    override fun scheduleDailyChallengeNotification(nextNotificationTimestamp: Long) {
        dailyChallengeNotificationTimestamp = nextNotificationTimestamp
    }

    override fun calculateNextDailyChallengeNotificationTimestamp(): Long =
        nextDailyChallengeNotificationTimestamp

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

    companion object {
        private const val FAKE_NEXT_DAILY_CHALLENGE_NOTIFICATION_TIMESTAMP = 1_000_000L
    }
}
