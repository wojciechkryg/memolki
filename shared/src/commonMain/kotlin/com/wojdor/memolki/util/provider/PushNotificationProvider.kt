package com.wojdor.memolki.util.provider

interface PushNotificationProvider {

    suspend fun subscribeToTopics()
}
