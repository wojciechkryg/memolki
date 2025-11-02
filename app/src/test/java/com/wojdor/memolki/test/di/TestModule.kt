package com.wojdor.memolki.test.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import com.google.android.play.core.review.ReviewManager
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.card.AllCardPairsDataSource
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakeDataStore
import com.wojdor.memolki.test.fake.FakeEncryptor
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@Module(includes = [TestCoroutineModule::class])
@InstallIn(SingletonComponent::class)
abstract class TestModule {

    @Binds
    @Singleton
    abstract fun bindEncryptor(mockEncryptor: FakeEncryptor): Encryptor

    @Binds
    @Singleton
    abstract fun bindAllCardPairsDataSource(fakeAllCardPairsDataSource: FakeAllCardPairsDataSource): AllCardPairsDataSource

    @Binds
    @Singleton
    abstract fun bindDataStore(fakeDataStore: FakeDataStore): DataStore<Preferences>

    companion object {
        @Provides
        @Singleton
        fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

        @Provides
        @Singleton
        @ApplicationContext
        fun provideContext(): Context = relaxedMockk()

        @Provides
        @Singleton
        fun provideRandom(): Random = Random(0)

        @Provides
        @Singleton
        fun provideHapticFeedback(): HapticFeedback = relaxedMockk()

        @Provides
        @Singleton
        fun provideBackgroundMusicPlayer(): BackgroundMusicPlayer = relaxedMockk()

        @Provides
        @Singleton
        fun provideCoinsPlayer(): CoinsPlayer = relaxedMockk()

        @Provides
        @Singleton
        fun provideCardFlipPlayer(): CardFlipPlayer = relaxedMockk()

        @Provides
        @Singleton
        fun provideCardPairMatchedPlayer(): CardPairMatchedPlayer = relaxedMockk()

        @Provides
        @Singleton
        fun provideLevelCompletePlayer(): LevelCompletePlayer = relaxedMockk()

        @Provides
        @Singleton
        fun provideAllRewardedAds(): AllRewardedAds = relaxedMockk()

        @Provides
        @Singleton
        fun provideBillingHandler(): BillingHandler = relaxedMockk()

        @Provides
        @Singleton
        fun provideReviewManager(): ReviewManager = relaxedMockk()

        @Provides
        @Singleton
        fun provideGooglePlayGames(): GooglePlayGames = relaxedMockk()
    }
}
