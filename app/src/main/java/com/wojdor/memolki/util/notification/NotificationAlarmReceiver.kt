package com.wojdor.memolki.util.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.core.net.toUri
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.app.AppActivity
import com.wojdor.memolki.util.notification.DeepLinkBuilder
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.AD_REWARD_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.NOTIFICATION_CHANNEL_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.REMINDER_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.STREAK_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_AD_REWARD
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_REMINDER
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.TYPE_STREAK
import dagger.hilt.android.EntryPointAccessors
import java.util.Locale

class NotificationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationAlarmReceiverEntryPoint::class.java
        )
        val localizedContext = createLocalizedContext(context, entryPoint)
        val type = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE) ?: return
        when (type) {
            TYPE_REMINDER -> handleReminderNotification(context, localizedContext, entryPoint)
            TYPE_AD_REWARD -> handleAdRewardNotification(context, localizedContext, entryPoint)
            TYPE_STREAK -> handleStreakNotification(context, localizedContext, entryPoint)
        }
    }

    private fun createLocalizedContext(
        context: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ): Context {
        val languageTag = entryPoint.localeProvider().getLanguageTag()
        val locale = Locale(languageTag)
        val config = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }

    private fun handleReminderNotification(
        context: Context,
        localizedContext: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        entryPoint.notificationScheduler().scheduleReminderNotification()
        val random = entryPoint.random()
        val titles = localizedContext.resources.getStringArray(R.array.notification_reminder_titles)
        val bodies = localizedContext.resources.getStringArray(R.array.notification_reminder_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createLauncherIntent(context, TYPE_REMINDER)
        showNotification(entryPoint, REMINDER_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun handleAdRewardNotification(
        context: Context,
        localizedContext: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        val random = entryPoint.random()
        val titles = localizedContext.resources.getStringArray(R.array.notification_ad_reward_titles)
        val bodies = localizedContext.resources.getStringArray(R.array.notification_ad_reward_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context, TYPE_AD_REWARD)
        showNotification(entryPoint, AD_REWARD_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun handleStreakNotification(
        context: Context,
        localizedContext: Context,
        entryPoint: NotificationAlarmReceiverEntryPoint
    ) {
        val random = entryPoint.random()
        val titles = localizedContext.resources.getStringArray(R.array.notification_daily_streak_titles)
        val bodies = localizedContext.resources.getStringArray(R.array.notification_daily_streak_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context, TYPE_STREAK)
        showNotification(entryPoint, STREAK_NOTIFICATION_ID, title, body, contentIntent)
    }

    private fun showNotification(
        entryPoint: NotificationAlarmReceiverEntryPoint,
        notificationId: Int,
        title: String,
        body: String,
        contentIntent: PendingIntent
    ) {
        entryPoint.notificationCreator().showNotification(notificationId, title, body, contentIntent)
    }

    private fun createLauncherIntent(context: Context, notificationType: String): PendingIntent {
        val intent = Intent(context, AppActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, notificationType)
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createShopDeepLinkIntent(context: Context, notificationType: String): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            DeepLinkBuilder.buildScreenUri(DeepLinkBuilder.SCREEN_SHOP).toUri(),
            context,
            AppActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, notificationType)
        }
        return PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
