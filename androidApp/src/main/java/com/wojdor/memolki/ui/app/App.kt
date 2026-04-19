package com.wojdor.memolki.ui.app

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.di.appKoinModule
import com.wojdor.memolki.ui.ads.AdsInitializer
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val adsInitializer: AdsInitializer by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appKoinModule)
        }
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
