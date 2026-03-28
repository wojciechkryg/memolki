package com.wojdor.memolki.util.notification

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.app.AppActivity
import com.wojdor.memolki.ui.app.AppNavigation.SHOP_DEEP_LINK
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.AD_REWARD_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.REMINDER_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.NOTIFICATION_CHANNEL_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.STREAK_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_AD_REWARD
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_REMINDER
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_STREAK
import dagger.hilt.android.EntryPointAccessors

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationAlarmReceiverEntryPoint::class.java
        )
        val type = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE) ?: return
        when (type) {
            TYPE_REMINDER -> handleReminderNotification(context, entryPoint)
            TYPE_AD_REWARD -> handleAdRewardNotification(context, entryPoint)
            TYPE_STREAK -> handleStreakNotification(context, entryPoint)
        }
    }

    private fun handleReminderNotification(
        context: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        entryPoint.notificationScheduler().scheduleReminderNotification()
        val random = entryPoint.random()
        val titles = context.resources.getStringArray(R.array.notification_reminder_titles)
        val bodies = context.resources.getStringArray(R.array.notification_reminder_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createLauncherIntent(context)
        showNotification(context, REMINDER_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun handleAdRewardNotification(
        context: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        val random = entryPoint.random()
        val titles = context.resources.getStringArray(R.array.notification_ad_reward_titles)
        val bodies = context.resources.getStringArray(R.array.notification_ad_reward_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context)
        showNotification(context, AD_REWARD_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun handleStreakNotification(
        context: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        val random = entryPoint.random()
        val titles = context.resources.getStringArray(R.array.notification_daily_streak_titles)
        val bodies = context.resources.getStringArray(R.array.notification_daily_streak_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context)
        showNotification(context, STREAK_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        body: String,
        contentIntent: PendingIntent
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) return
        }
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun createLauncherIntent(context: Context): PendingIntent {
        val intent = Intent(context, AppActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createShopDeepLinkIntent(context: Context): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            SHOP_DEEP_LINK.toUri(),
            context,
            AppActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
