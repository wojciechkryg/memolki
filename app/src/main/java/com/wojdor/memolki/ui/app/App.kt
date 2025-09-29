package com.wojdor.memolki.ui.app

import android.app.Application
import com.wojdor.memolki.ui.ads.AdsInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var adsInitializer: AdsInitializer

    override fun onCreate() {
        super.onCreate()
        initializeAds()
    }

    private fun initializeAds() {
        adsInitializer.initialize()
    }
}
