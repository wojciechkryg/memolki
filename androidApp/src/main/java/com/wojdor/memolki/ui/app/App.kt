package com.wojdor.memolki.ui.app

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.ui.ads.AdsInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var adsInitializer: AdsInitializer

    override fun onCreate() {
        super.onCreate()
        disableFirebaseInDebug()
        initializeAds()
    }

    private fun disableFirebaseInDebug() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initializeAds() {
        adsInitializer.initialize()
    }
}
