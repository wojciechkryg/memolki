package com.wojdor.memolki.util.notification

interface NotificationScheduler {
    fun scheduleReminderNotification()
    fun scheduleAdRewardNotification()
    fun scheduleStreakNotification()
    fun scheduleDailyChallengeNotification(nextNotificationTimestamp: Long)
    fun calculateNextDailyChallengeNotificationTimestamp(): Long
    fun cancelAdRewardNotification()
    fun dismissVisibleNotifications()
    fun createNotificationChannel()
    fun hasNotificationPermission(): Boolean

    companion object {
        const val SHOP_AD_COOLDOWN_MS = 30 * 60 * 1000L
    }
}
