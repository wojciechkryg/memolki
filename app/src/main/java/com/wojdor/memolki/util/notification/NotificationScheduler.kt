package com.wojdor.memolki.util.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wojdor.memolki.util.provider.PermissionProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

open class NotificationScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val random: Random,
    private val notificationCreator: NotificationCreator,
    private val permissionProvider: PermissionProvider
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        createNotificationChannel()
        scheduleReminderNotification()
    }

    override fun onResume(owner: LifecycleOwner) {
        dismissVisibleNotifications()
    }

    open fun scheduleReminderNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            ?: return
        val triggerAt = calculateNextReminderTriggerTime()
        val pendingIntent = createPendingIntent(TYPE_REMINDER, REMINDER_ALARM_REQUEST_CODE)
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            DAILY_WINDOW_MS,
            pendingIntent
        )
    }

    open fun scheduleAdRewardNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            ?: return
        val triggerAt = System.currentTimeMillis() + SHOP_AD_COOLDOWN_MS
        val pendingIntent = createPendingIntent(TYPE_AD_REWARD, AD_REWARD_ALARM_REQUEST_CODE)
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            AD_REWARD_WINDOW_MS,
            pendingIntent
        )
    }

    open fun scheduleStreakNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            ?: return
        val triggerAt = calculateNextStreakTriggerTime()
        val pendingIntent = createPendingIntent(TYPE_STREAK, STREAK_ALARM_REQUEST_CODE)
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            DAILY_WINDOW_MS,
            pendingIntent
        )
    }

    open fun cancelAdRewardNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            ?: return
        val pendingIntent = createPendingIntent(TYPE_AD_REWARD, AD_REWARD_ALARM_REQUEST_CODE)
        alarmManager.cancel(pendingIntent)
    }

    open fun dismissVisibleNotifications() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return
        notificationManager.cancel(REMINDER_NOTIFICATION_ID)
        notificationManager.cancel(AD_REWARD_NOTIFICATION_ID)
        notificationManager.cancel(STREAK_NOTIFICATION_ID)
    }

    open fun createNotificationChannel() {
        notificationCreator.createNotificationChannel()
    }

    open fun hasNotificationPermission(): Boolean {
        return permissionProvider.hasNotificationPermission()
    }

    private fun calculateNextStreakTriggerTime(): Long {
        val randomHour = random.nextInt(DAILY_WINDOW_START_HOUR, DAILY_WINDOW_END_HOUR)
        val randomMinute = random.nextInt(MINUTES_IN_HOUR)
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, STREAK_INTERVAL_DAYS)
            set(Calendar.HOUR_OF_DAY, randomHour)
            set(Calendar.MINUTE, randomMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun calculateNextReminderTriggerTime(): Long {
        val randomHour = random.nextInt(DAILY_WINDOW_START_HOUR, DAILY_WINDOW_END_HOUR)
        val randomMinute = random.nextInt(MINUTES_IN_HOUR)
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, REMINDER_INTERVAL_DAYS)
            set(Calendar.HOUR_OF_DAY, randomHour)
            set(Calendar.MINUTE, randomMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun createPendingIntent(type: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, NotificationAlarmReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_TYPE, type)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        internal const val NOTIFICATION_CHANNEL_ID = "reminders"
        internal const val REMINDER_NOTIFICATION_ID = 2001
        internal const val AD_REWARD_NOTIFICATION_ID = 2002
        internal const val STREAK_NOTIFICATION_ID = 2003
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"
        internal const val TYPE_REMINDER = "reminder"
        internal const val TYPE_AD_REWARD = "ad_reward"
        internal const val TYPE_STREAK = "streak"
        internal const val TYPE_PUSH = "push"
        internal const val SHOP_AD_COOLDOWN_MS = 30 * 60 * 1000L
        private const val AD_REWARD_WINDOW_MS = 30 * 60 * 1000L
        private const val REMINDER_INTERVAL_DAYS = 3
        private const val DAILY_WINDOW_START_HOUR = 14
        private const val DAILY_WINDOW_END_HOUR = 20
        private const val DAILY_WINDOW_MS = 60 * 60 * 1000L
        private const val REMINDER_ALARM_REQUEST_CODE = 1001
        private const val AD_REWARD_ALARM_REQUEST_CODE = 1002
        private const val STREAK_ALARM_REQUEST_CODE = 1003
        private const val STREAK_INTERVAL_DAYS = 1
        private const val MINUTES_IN_HOUR = 60
    }
}
