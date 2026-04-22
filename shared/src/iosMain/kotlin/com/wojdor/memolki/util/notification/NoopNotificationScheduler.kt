package com.wojdor.memolki.util.notification

// TODO(kmp-ios): replace with a UserNotifications-backed impl when iOS notifications ship.
class NoopNotificationScheduler : NotificationScheduler {
    override fun scheduleReminderNotification() = Unit
    override fun scheduleAdRewardNotification() = Unit
    override fun scheduleStreakNotification() = Unit
    override fun scheduleDailyChallengeNotification(nextNotificationTimestamp: Long) = Unit
    override fun calculateNextDailyChallengeNotificationTimestamp(): Long = 0L
    override fun cancelAdRewardNotification() = Unit
    override fun dismissVisibleNotifications() = Unit
    override fun createNotificationChannel() = Unit
    override fun hasNotificationPermission(): Boolean = false
}
