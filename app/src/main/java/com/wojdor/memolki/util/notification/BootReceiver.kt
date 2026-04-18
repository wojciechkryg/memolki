package com.wojdor.memolki.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wojdor.memolki.util.extension.goAsyncIo
import dagger.hilt.android.EntryPointAccessors

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationAlarmReceiverEntryPoint::class.java
        )
        entryPoint.notificationScheduler().scheduleReminderNotification()
        entryPoint.notificationScheduler().scheduleStreakNotification()
        goAsyncIo("Failed to restore daily challenge notification") {
            val nextNotificationTimestamp = entryPoint.notificationRepository()
                .getNextDailyChallengeNotificationTimestamp()
            if (nextNotificationTimestamp > System.currentTimeMillis()) {
                entryPoint.notificationScheduler()
                    .scheduleDailyChallengeNotification(nextNotificationTimestamp)
            }
        }
    }
}
