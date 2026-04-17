package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val encryptor: Encryptor,
    private val notificationLocalDataSource: NotificationLocalDataSource
) {

    suspend fun getLastShownTimestamp(): Long {
        val encryptedValue = notificationLocalDataSource.encryptedLastShownTimestamp.first()
        return decryptLong(encryptedValue)
    }

    suspend fun setLastShownTimestamp(timestamp: Long) {
        notificationLocalDataSource.setEncryptedLastShownTimestamp(encryptor.encrypt(timestamp))
    }

    suspend fun getNextDailyChallengeNotificationTimestamp(): Long {
        val encryptedValue =
            notificationLocalDataSource.encryptedNextDailyChallengeNotificationTimestamp.first()
        return decryptLong(encryptedValue)
    }

    suspend fun setNextDailyChallengeNotificationTimestamp(timestamp: Long) {
        notificationLocalDataSource.setEncryptedNextDailyChallengeNotificationTimestamp(
            encryptor.encrypt(timestamp)
        )
    }

    private suspend fun decryptLong(encryptedValue: String?): Long {
        return if (encryptedValue.isNullOrEmpty()) {
            DEFAULT_LONG_VALUE
        } else {
            try {
                encryptor.decrypt(encryptedValue)
            } catch (error: Exception) {
                logE("Decryption error", error)
                DEFAULT_LONG_VALUE
            }
        }
    }

    companion object {
        private const val DEFAULT_LONG_VALUE = 0L
    }
}
