package com.wojdor.memolki.util.provider

import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import kotlinx.coroutines.flow.first
import platform.Foundation.NSBundle

class IosPushNotificationProvider(
    private val localeProvider: LocaleProvider,
    private val userLocalDataSource: UserLocalDataSource
) : PushNotificationProvider {

    override suspend fun subscribeToTopics() {
        val flavor = bundleIdentifierFlavor() ?: return
        val language = localeProvider.getLanguageTag()
        runCatching {
            Firebase.messaging.subscribeToTopic(flavor)
            val previousLanguage = userLocalDataSource.fcmLanguageTopic.first()
            if (previousLanguage != null && previousLanguage != language) {
                Firebase.messaging.unsubscribeFromTopic("lang_$previousLanguage")
            }
            Firebase.messaging.subscribeToTopic("lang_$language")
            userLocalDataSource.setFcmLanguageTopic(language)
        }
    }

    private fun bundleIdentifierFlavor(): String? =
        NSBundle.mainBundle.bundleIdentifier?.substringAfterLast('.')?.lowercase()
}
