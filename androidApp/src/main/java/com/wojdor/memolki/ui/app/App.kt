package com.wojdor.memolki.ui.app

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.di.DATA_STORE_NAME
import com.wojdor.memolki.di.appKoinModule
import com.wojdor.memolki.ui.ads.AdsInitializer
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File

class App : Application() {

    private val adsInitializer: AdsInitializer by inject()

    override fun onCreate() {
        super.onCreate()
        migrateLegacyDataStoreFile()
        startKoin {
            androidContext(this@App)
            modules(appKoinModule)
        }
        disableFirebaseInDebug()
        initializeAds()
    }

    // Must run before Koin creates the DataStore. Renames the legacy "data_store" file to the new
    // DATA_STORE_NAME so existing users keep their encrypted coins/streaks/unlocked cards intact.
    // TODO(migration): remove this + LEGACY_DATA_STORE_NAME once the rename has been in production
    // long enough that effectively all users have opened the app and the legacy file is gone.
    // Safe to drop ~6-12 months after the release that introduced the rename.
    private fun migrateLegacyDataStoreFile() {
        val datastoreDir = File(filesDir, "datastore")
        val legacyFile = File(datastoreDir, "$LEGACY_DATA_STORE_NAME.preferences_pb")
        val newFile = File(datastoreDir, "$DATA_STORE_NAME.preferences_pb")
        if (legacyFile.exists() && !newFile.exists()) {
            legacyFile.renameTo(newFile)
        }
    }

    private fun disableFirebaseInDebug() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initializeAds() {
        adsInitializer.initialize()
    }

    private companion object {
        const val LEGACY_DATA_STORE_NAME = "data_store"
    }
}
