package com.wojdor.memolki.util.provider

import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import kotlinx.coroutines.flow.first

class IosPushNotificationProvider(
    private val packageNameProvider: PackageNameProvider,
    private val localeProvider: LocaleProvider,
    private val userLocalDataSource: UserLocalDataSource
) : PushNotificationProvider {

    override suspend fun subscribeToTopics() {
        val flavorTopic = packageNameProvider.providePackageName()
            .substringAfterLast('.')
            .lowercase()
            .takeIf { it.isNotEmpty() } ?: return
        val language = localeProvider.getLanguageTag()
        runCatching {
            Firebase.messaging.subscribeToTopic(flavorTopic)
            val previousLanguage = userLocalDataSource.fcmLanguageTopic.first()
            if (previousLanguage != language) {
                if (previousLanguage != null) {
                    Firebase.messaging.unsubscribeFromTopic("lang_$previousLanguage")
                }
                Firebase.messaging.subscribeToTopic("lang_$language")
                userLocalDataSource.setFcmLanguageTopic(language)
            }
        }
    }
}
