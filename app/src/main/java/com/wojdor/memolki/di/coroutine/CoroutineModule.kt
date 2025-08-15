package com.wojdor.memolki.di.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main
}
