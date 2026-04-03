package com.wojdor.memolki.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.wojdor.memolki.data.crypto.BaseEncryptor
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.card.AllCardPairsLocalDataSource
import com.wojdor.memolki.data.local.database.AppDatabase
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindEncryptor(baseEncryptor: BaseEncryptor): Encryptor

    @Binds
    abstract fun bindAllCardPairsDataSource(allCardPairsLocalDataSource: AllCardPairsLocalDataSource): AllCardPairsDataSource

    companion object {

        private const val DATA_STORE_NAME = "data_store"
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.dataStore

        private const val DATABASE_NAME = "memolki_database"

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

        @Provides
        fun provideDailyChallengeDao(database: AppDatabase): DailyChallengeDao =
            database.dailyChallengeDao()
    }
}
