package com.wojdor.memolki.util.provider

// TODO(kmp-ios): wire to APNs / FCM iOS topic subscriptions when iOS push ships.
class IosPushNotificationProvider : PushNotificationProvider {
    override suspend fun subscribeToTopics() = Unit
}
