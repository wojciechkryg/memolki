package com.wojdor.memolki.ui.ads

import android.content.Context
import com.google.android.gms.ads.AgeRestrictedTreatment
import com.google.android.gms.ads.MobileAds
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
                .setAgeRestrictedTreatment(AgeRestrictedTreatment.CHILD)
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            MobileAds.initialize(context)
        }
    }

    private fun loadAllAds() {
        allRewardedAds.loadAllAds()
    }
}
