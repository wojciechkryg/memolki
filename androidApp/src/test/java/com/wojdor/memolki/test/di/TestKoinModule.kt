package com.wojdor.memolki.test.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.messaging.FirebaseMessaging
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.di.DefaultDispatcher
import com.wojdor.memolki.di.MainDispatcher
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakeAppForegroundProvider
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import com.wojdor.memolki.test.fake.FakeDataStore
import com.wojdor.memolki.test.fake.FakeEncryptor
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.test.fake.FakePushNotificationProvider
import com.wojdor.memolki.test.fake.FakeStringProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.gameservices.GameServices
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.TimeProvider
import com.wojdor.memolki.util.resource.StringProvider
import com.wojdor.memolki.util.review.InAppReviewer
import io.mockk.every
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
val testKoinModule = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineDispatcher>(DefaultDispatcher) { get<CoroutineDispatcher>() }
    single<CoroutineDispatcher>(MainDispatcher) { get<CoroutineDispatcher>() }

    single { SavedStateHandle() }
    single<Context> { relaxedMockk() }
    single { Random(0) }

    singleOf(::FakeEncryptor) { bind<Encryptor>() }
    singleOf(::FakeAllCardPairsDataSource) { bind<AllCardPairsDataSource>() }
    singleOf(::FakeDataStore) { bind<DataStore<Preferences>>() }
    singleOf(::FakeAppForegroundProvider) { bind<AppForegroundProvider>() }
    singleOf(::FakeAppInstalledProvider) { bind<AppInstalledProvider>() }
    singleOf(::FakeLocaleProvider) { bind<LocaleProvider>() }
    singleOf(::FakeNotificationScheduler) { bind<NotificationScheduler>() }
    singleOf(::FakePackageNameProvider) { bind<PackageNameProvider>() }
    singleOf(::FakePermissionProvider) { bind<PermissionProvider>() }
    singleOf(::FakePushNotificationProvider) { bind<PushNotificationProvider>() }
    singleOf(::FakeTimeProvider) { bind<TimeProvider>() }
    singleOf(::FakeStringProvider) { bind<StringProvider>() }

    single<HapticFeedback> { relaxedMockk() }
    single<BackgroundMusicPlayer> { relaxedMockk() }
    single<CoinsPlayer> { relaxedMockk() }
    single<CardFlipPlayer> { relaxedMockk() }
    single<CardPairMatchedPlayer> { relaxedMockk() }
    single<LevelCompletePlayer> { relaxedMockk() }
    single<AllRewardedAds> {
        relaxedMockk<AllRewardedAds>().also { ads ->
            listOf(ads.endGameCoinsAd, ads.collectionCardPairAd, ads.shopCoinsAd).forEach { ad ->
                every { ad.loadAndNotify(any(), any()) } answers {
                    secondArg<(Boolean) -> Unit>().invoke(false)
                }
            }
        }
    }
    single<BillingHandler> { relaxedMockk() }
    single<InAppReviewer> { relaxedMockk() }
    single<GameServices> { relaxedMockk() }
    single<FirebaseMessaging> { relaxedMockk() }
    single<Analytics> { relaxedMockk() }
    single<DailyChallengeDao> { relaxedMockk() }
    single<LocalEncryptorKeyStore> { relaxedMockk() }
}
