package com.wojdor.memolki.di

import com.wojdor.memolki.ui.ads.IosRewardedAdsBridge
import com.wojdor.memolki.util.billing.IosStoreKitBridge
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(
    storeKitBridge: IosStoreKitBridge,
    rewardedAdsBridge: IosRewardedAdsBridge
) {
    startKoin {
        modules(
            sharedKoinModule,
            iosKoinModule,
            module {
                single { storeKitBridge }
                single { rewardedAdsBridge }
            }
        )
    }
}
