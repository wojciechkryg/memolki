package com.wojdor.memolki.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedKoinModule, iosKoinModule)
    }
}
