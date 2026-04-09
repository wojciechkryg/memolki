package com.wojdor.memolki.test.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import com.google.android.play.core.review.ReviewManager
import com.google.firebase.messaging.FirebaseMessaging
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import com.wojdor.memolki.test.fake.FakeDataStore
import com.wojdor.memolki.test.fake.FakeEncryptor
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.test.fake.FakePushNotificationProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import io.mockk.every
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.TimeProvider
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
    abstract fun bindEncryptor(fakeEncryptor: FakeEncryptor): Encryptor

    @Binds
    @Singleton
    abstract fun bindAllCardPairsDataSource(fakeAllCardPairsDataSource: FakeAllCardPairsDataSource): AllCardPairsDataSource

    @Binds
    @Singleton
    abstract fun bindDataStore(fakeDataStore: FakeDataStore): DataStore<Preferences>

    @Binds
    @Singleton
    abstract fun bindPackageNameProvider(fakePackageNameProvider: FakePackageNameProvider): PackageNameProvider

    @Binds
    @Singleton
    abstract fun bindAppInstalledProvider(fakeAppInstalledProvider: FakeAppInstalledProvider): AppInstalledProvider

    @Binds
    @Singleton
    abstract fun bindLocaleProvider(fakeLocaleProvider: FakeLocaleProvider): LocaleProvider

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(fakeNotificationScheduler: FakeNotificationScheduler): NotificationScheduler

    @Binds
    @Singleton
    abstract fun bindPermissionProvider(fakePermissionProvider: FakePermissionProvider): PermissionProvider

    @Binds
    @Singleton
    abstract fun bindPushNotificationProvider(fakePushNotificationProvider: FakePushNotificationProvider): PushNotificationProvider

    @Binds
    @Singleton
    abstract fun bindTimeProvider(fakeTimeProvider: FakeTimeProvider): TimeProvider

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
        fun provideAllRewardedAds(): AllRewardedAds = relaxedMockk<AllRewardedAds>().also { ads ->
            listOf(ads.endGameCoinsAd, ads.collectionCardPairAd, ads.shopCoinsAd).forEach { ad ->
                every { ad.loadAndNotify(any(), any()) } answers {
                    secondArg<(Boolean) -> Unit>().invoke(false)
                }
            }
        }

        @Provides
        @Singleton
        fun provideBillingHandler(): BillingHandler = relaxedMockk()

        @Provides
        @Singleton
        fun provideReviewManager(): ReviewManager = relaxedMockk()

        @Provides
        @Singleton
        fun provideGooglePlayGames(): GooglePlayGames = relaxedMockk()

        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = relaxedMockk()

        @Provides
        @Singleton
        fun provideAnalytics(): Analytics = relaxedMockk()

        @Provides
        @Singleton
        fun provideDailyChallengeDao(): DailyChallengeDao = relaxedMockk()

        @Provides
        @Singleton
        fun provideLocalEncryptorKeyStore(): LocalEncryptorKeyStore = relaxedMockk()
    }
}
