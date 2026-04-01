package com.wojdor.memolki.util.provider

import com.google.firebase.messaging.FirebaseMessaging
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

open class PushNotificationProvider @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val localeProvider: LocaleProvider,
    private val userLocalDataSource: UserLocalDataSource
) {

    open suspend fun subscribeToTopics() {
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
