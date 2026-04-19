package com.wojdor.memolki.ui.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.wojdor.memolki.di.coroutine.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AdsInitializer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val allRewardedAds: AllRewardedAds
) {

    fun initialize() {
        initializeAds()
        loadAllAds()
    }

    private fun initializeAds() {
        CoroutineScope(coroutineDispatcher).launch {
            val requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            MobileAds.initialize(context)
        }
    }

    private fun loadAllAds() {
        allRewardedAds.loadAllAds()
    }
}
