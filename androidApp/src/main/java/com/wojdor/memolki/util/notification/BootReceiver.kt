package com.wojdor.memolki.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.util.extension.goAsyncIo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationScheduler: NotificationScheduler by inject()
    private val notificationRepository: NotificationRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        notificationScheduler.scheduleReminderNotification()
        notificationScheduler.scheduleStreakNotification()
        goAsyncIo("Failed to restore daily challenge notification") {
            val nextNotificationTimestamp = notificationRepository
                .getNextDailyChallengeNotificationTimestamp()
            if (nextNotificationTimestamp > System.currentTimeMillis()) {
                notificationScheduler
                    .scheduleDailyChallengeNotification(nextNotificationTimestamp)
            }
        }
    }
}
