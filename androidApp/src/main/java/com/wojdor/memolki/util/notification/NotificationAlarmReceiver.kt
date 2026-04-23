package com.wojdor.memolki.util.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.core.net.toUri
import com.wojdor.memolki.R
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.ui.app.AppActivity
import com.wojdor.memolki.util.extension.goAsyncIo
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.AD_REWARD_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.DAILY_CHALLENGE_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.REMINDER_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.STREAK_NOTIFICATION_ID
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.TYPE_AD_REWARD
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.TYPE_DAILY_CHALLENGE
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.TYPE_REMINDER
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.TYPE_STREAK
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale
import kotlin.random.Random

class NotificationAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationCreator: NotificationCreator by inject()
    private val notificationScheduler: NotificationScheduler by inject()
    private val dailyChallengeRepository: DailyChallengeRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    private val userRepository: UserRepository by inject()
    private val appForegroundProvider: AppForegroundProvider by inject()
    private val localeProvider: LocaleProvider by inject()
    private val timeProvider: TimeProvider by inject()
    private val random: Random by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val localizedContext = createLocalizedContext(context)
        val type = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE) ?: return
        goAsyncIo("Failed to handle notification") {
            when (type) {
                TYPE_REMINDER -> handleReminderNotification(context, localizedContext)
                TYPE_AD_REWARD -> handleAdRewardNotification(context, localizedContext)
                TYPE_STREAK -> handleStreakNotification(context, localizedContext)
                TYPE_DAILY_CHALLENGE -> handleDailyChallengeNotification(context, localizedContext)
            }
        }
    }

    private fun createLocalizedContext(context: Context): Context {
        val languageTag = localeProvider.getLanguageTag()
        val locale = Locale.forLanguageTag(languageTag)
        val config = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }

    private suspend fun handleReminderNotification(
        context: Context,
        localizedContext: Context
    ) {
        notificationScheduler.scheduleReminderNotification()
        val titles = localizedContext.resources.getStringArray(R.array.notification_reminder_titles)
        val bodies = localizedContext.resources.getStringArray(R.array.notification_reminder_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createLauncherIntent(context, TYPE_REMINDER)
        showNotification(REMINDER_NOTIFICATION_ID, title, body, contentIntent)
    }

    private suspend fun handleAdRewardNotification(
        context: Context,
        localizedContext: Context
    ) {
        val titles =
            localizedContext.resources.getStringArray(R.array.notification_ad_reward_titles)
        val bodies =
            localizedContext.resources.getStringArray(R.array.notification_ad_reward_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context, TYPE_AD_REWARD)
        showNotification(AD_REWARD_NOTIFICATION_ID, title, body, contentIntent)
    }

    private suspend fun handleStreakNotification(
        context: Context,
        localizedContext: Context
    ) {
        val streakCount = userRepository.getDailyStreakCount().first()
        if (streakCount <= 0) return
        val titles =
            localizedContext.resources.getStringArray(R.array.notification_daily_streak_titles)
        val bodies =
            localizedContext.resources.getStringArray(R.array.notification_daily_streak_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createShopDeepLinkIntent(context, TYPE_STREAK)
        showNotification(STREAK_NOTIFICATION_ID, title, body, contentIntent)
    }

    private suspend fun handleDailyChallengeNotification(
        context: Context,
        localizedContext: Context
    ) {
        val epochDay = timeProvider.currentLocalDate().toEpochDays()
        val hasPlayed = dailyChallengeRepository.hasPlayed(epochDay)
        if (hasPlayed) return
        val titles =
            localizedContext.resources.getStringArray(R.array.notification_daily_challenge_titles)
        val bodies =
            localizedContext.resources.getStringArray(R.array.notification_daily_challenge_bodies)
        val title = titles[random.nextInt(titles.size)]
        val body = bodies[random.nextInt(bodies.size)]
        val contentIntent = createDailyChallengeDeepLinkIntent(context)
        showNotification(
            DAILY_CHALLENGE_NOTIFICATION_ID,
            title,
            body,
            contentIntent,
            skipGapCheck = true
        )
    }

    private suspend fun showNotification(
        notificationId: Int,
        title: String,
        body: String,
        contentIntent: PendingIntent,
        skipGapCheck: Boolean = false
    ) {
        if (appForegroundProvider.isAppInForeground()) return
        val now = System.currentTimeMillis()
        val lastShown = notificationRepository.getLastShownTimestamp()
        if (!skipGapCheck && now - lastShown < MIN_NOTIFICATION_GAP_MS) return
        notificationRepository.setLastShownTimestamp(now)
        notificationCreator.showNotification(notificationId, title, body, contentIntent)
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

    private fun createShopDeepLinkIntent(
        context: Context,
        notificationType: String
    ): PendingIntent {
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

    private fun createDailyChallengeDeepLinkIntent(context: Context): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            DeepLinkBuilder.buildScreenUri(DeepLinkBuilder.SCREEN_DAILY_CHALLENGE).toUri(),
            context,
            AppActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, TYPE_DAILY_CHALLENGE)
        }
        return PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val MIN_NOTIFICATION_GAP_MS = 60 * 60 * 1000L
    }
}
