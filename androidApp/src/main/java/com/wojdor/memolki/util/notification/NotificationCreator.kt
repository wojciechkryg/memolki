package com.wojdor.memolki.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wojdor.memolki.R
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.NOTIFICATION_CHANNEL_ID
import com.wojdor.memolki.util.provider.PermissionProvider

class NotificationCreator(
    private val context: Context,
    private val permissionProvider: PermissionProvider
) {

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification_channel_reminders),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        notificationId: Int,
        title: String?,
        body: String?,
        contentIntent: PendingIntent
    ) {
        if (!permissionProvider.hasNotificationPermission()) return
        val hasExisting = cancelOtherActiveNotifications(notificationId)
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setSilent(hasExisting)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun cancelOtherActiveNotifications(notificationId: Int): Boolean {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return false
        val active = manager.activeNotifications
        active.filter { it.id != notificationId }.forEach { manager.cancel(it.id) }
        return active.isNotEmpty()
    }
}
