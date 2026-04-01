package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.provider.PushNotificationProvider
import io.mockk.mockk
import javax.inject.Inject

class FakePushNotificationProvider @Inject constructor() : PushNotificationProvider(
    mockk(),
    FakePackageNameProvider(),
    FakeLocaleProvider(),
    mockk()
) {

    var topicsSubscribed = false
        private set

    override suspend fun subscribeToTopics() {
        topicsSubscribed = true
    }
}
