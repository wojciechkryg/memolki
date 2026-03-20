package com.wojdor.memolki.util.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wojdor.memolki.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

open class NotificationScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val random: Random
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        createNotificationChannel()
        scheduleDailyNotification()
    }

    override fun onResume(owner: LifecycleOwner) {
        dismissVisibleNotifications()
    }

    open fun scheduleDailyNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            ?: return
        val triggerAt = calculateNextDailyTriggerTime()
        val pendingIntent = createPendingIntent(TYPE_DAILY, DAILY_ALARM_REQUEST_CODE)
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
        notificationManager.cancel(DAILY_NOTIFICATION_ID)
        notificationManager.cancel(AD_REWARD_NOTIFICATION_ID)
    }

    open fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_channel_reminders),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    open fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun calculateNextDailyTriggerTime(): Long {
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
        internal const val DAILY_NOTIFICATION_ID = 2001
        internal const val AD_REWARD_NOTIFICATION_ID = 2002
        internal const val EXTRA_NOTIFICATION_TYPE = "notification_type"
        internal const val TYPE_DAILY = "daily"
        internal const val TYPE_AD_REWARD = "ad_reward"
        internal const val SHOP_AD_COOLDOWN_MS = 30 * 60 * 1000L
        private const val AD_REWARD_WINDOW_MS = 30 * 60 * 1000L
        private const val REMINDER_INTERVAL_DAYS = 3
        private const val DAILY_WINDOW_START_HOUR = 14
        private const val DAILY_WINDOW_END_HOUR = 20
        private const val DAILY_WINDOW_MS = 60 * 60 * 1000L
        private const val DAILY_ALARM_REQUEST_CODE = 1001
        private const val AD_REWARD_ALARM_REQUEST_CODE = 1002
        private const val MINUTES_IN_HOUR = 60
    }
}
