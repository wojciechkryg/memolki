package com.wojdor.memolki.util.notification

import android.app.PendingIntent
import android.content.Intent
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wojdor.memolki.ui.app.AppActivity
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import dagger.hilt.android.EntryPointAccessors

class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = remoteMessage.notification?.title ?: data["title"]
        val body = remoteMessage.notification?.body ?: data["body"]
        val screen = data[DeepLinkBuilder.EXTRA_SCREEN]
        val level = data[DeepLinkBuilder.EXTRA_LEVEL]
        val notificationCreator = EntryPointAccessors.fromApplication(
            applicationContext,
            PushNotificationServiceEntryPoint::class.java
        ).notificationCreator()
        notificationCreator.createNotificationChannel()
        notificationCreator.showNotification(
            notificationId = PUSH_NOTIFICATION_ID,
            title = title,
            body = body,
            contentIntent = createPendingIntent(screen, level)
        )
    }

    override fun onNewToken(token: String) = Unit

    private fun createPendingIntent(screen: String?, level: String?): PendingIntent {
        val deepLinkUri = DeepLinkBuilder.buildUri(screen, level)
        val intent = if (deepLinkUri != null) {
            Intent(Intent.ACTION_VIEW, deepLinkUri.toUri(), this, AppActivity::class.java)
        } else {
            Intent(this, AppActivity::class.java)
        }.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, NotificationScheduler.TYPE_PUSH)
        }
        return PendingIntent.getActivity(
            this,
            PUSH_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val PUSH_NOTIFICATION_ID = 3001
        private const val PUSH_REQUEST_CODE = 2
    }
}
