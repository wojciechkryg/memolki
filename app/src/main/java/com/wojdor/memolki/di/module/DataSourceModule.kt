package com.wojdor.memolki.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wojdor.memolki.data.local.card.AllCardPairsDataSource
import com.wojdor.memolki.data.local.card.AllCardPairsLocalDataSource
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

        private const val DATA_STORE_NAME = "data_store"
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): DataStore<Preferences> =
            context.dataStore
    }
}
