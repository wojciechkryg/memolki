package com.wojdor.memolki.data.local.datastore.notification

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

open class NotificationLocalDataSource(
    private val dataWrite: DataStore<Preferences>
) {
    private val dataRead: Flow<Preferences> = dataWrite.data.catch { _ ->
        emit(emptyPreferences())
    }

    val encryptedLastShownTimestamp: Flow<String?> =
        dataRead.map { it[Key.LAST_SHOWN_TIMESTAMP] }

    val encryptedNextDailyChallengeNotificationTimestamp: Flow<String?> =
        dataRead.map { it[Key.NEXT_DAILY_CHALLENGE_NOTIFICATION_TIMESTAMP] }

    open suspend fun setEncryptedLastShownTimestamp(encryptedValue: String) {
        dataWrite.edit { it[Key.LAST_SHOWN_TIMESTAMP] = encryptedValue }
    }

    open suspend fun setEncryptedNextDailyChallengeNotificationTimestamp(encryptedValue: String) {
        dataWrite.edit { it[Key.NEXT_DAILY_CHALLENGE_NOTIFICATION_TIMESTAMP] = encryptedValue }
    }

    private object Key {
        val LAST_SHOWN_TIMESTAMP =
            stringPreferencesKey("last_notification_shown_timestamp")
        val NEXT_DAILY_CHALLENGE_NOTIFICATION_TIMESTAMP =
            stringPreferencesKey("next_daily_challenge_notification_timestamp")
    }
}
