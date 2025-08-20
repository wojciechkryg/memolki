package com.wojdor.memolki.di.module

import android.content.Context
import android.content.SharedPreferences
import com.wojdor.memolki.data.source.card.local.AllCardPairsDataSource
import com.wojdor.memolki.data.source.card.local.AllCardPairsLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindAllCardPairsDataSource(allCardPairsLocalDataSource: AllCardPairsLocalDataSource): AllCardPairsDataSource

    companion object {

        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        private const val SHARED_PREFS_NAME = "shared_prefs"
    }
}
