package com.wojdor.memolki.test.di

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.di.coroutine.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object TestCoroutineModule {

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher = StandardTestDispatcher()

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(dispatcher: CoroutineDispatcher): CoroutineDispatcher = dispatcher

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(dispatcher: CoroutineDispatcher): CoroutineDispatcher = dispatcher

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(dispatcher: CoroutineDispatcher): CoroutineDispatcher = dispatcher
}
