package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PushNotificationProvider

class FakePushNotificationProvider : PushNotificationProvider {

    var topicsSubscribed = false
        private set

    override suspend fun subscribeToTopics() {
        topicsSubscribed = true
    }
}
