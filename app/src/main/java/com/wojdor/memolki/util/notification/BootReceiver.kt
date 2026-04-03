package com.wojdor.memolki.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
        entryPoint.notificationScheduler().scheduleDailyChallengeNotification()
    }
}
