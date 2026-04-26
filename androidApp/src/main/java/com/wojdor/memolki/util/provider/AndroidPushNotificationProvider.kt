package com.wojdor.memolki.util.provider

import com.google.firebase.messaging.FirebaseMessaging
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import kotlinx.coroutines.flow.first

// TODO(kmp-push): Android-only impl. FirebaseMessaging is Google's SDK (not GitLive —
// GitLive's firebase-messaging has no iOS topic API yet). Add IosPushNotificationProvider when iOS push ships.
open class AndroidPushNotificationProvider(
    private val firebaseMessaging: FirebaseMessaging,
    private val localeProvider: LocaleProvider,
    private val userLocalDataSource: UserLocalDataSource
) : PushNotificationProvider {

    override suspend fun subscribeToTopics() {
        val flavor = BuildConfig.FLAVOR.lowercase()
        val language = localeProvider.getLanguageTag()
        firebaseMessaging.subscribeToTopic(flavor)
        val previousLanguage = userLocalDataSource.fcmLanguageTopic.first()
        if (previousLanguage != null && previousLanguage != language) {
            firebaseMessaging.unsubscribeFromTopic("lang_$previousLanguage")
        }
        firebaseMessaging.subscribeToTopic("lang_$language")
        userLocalDataSource.setFcmLanguageTopic(language)
    }
}
