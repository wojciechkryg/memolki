package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PushNotificationProvider
import io.mockk.mockk

class FakePushNotificationProvider : PushNotificationProvider(
    mockk(),
    FakeLocaleProvider(),
    mockk()
) {

    var topicsSubscribed = false
        private set

    override suspend fun subscribeToTopics() {
        topicsSubscribed = true
    }
}
