package com.wojdor.memolki.test.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.di.DefaultDispatcher
import com.wojdor.memolki.di.MainDispatcher
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakeAllRewardedAds
import com.wojdor.memolki.test.fake.FakeAnalytics
import com.wojdor.memolki.test.fake.FakeAppForegroundProvider
import com.wojdor.memolki.test.fake.FakeAppInstalledProvider
import com.wojdor.memolki.test.fake.FakeBackgroundMusicPlayer
import com.wojdor.memolki.test.fake.FakeBillingHandler
import com.wojdor.memolki.test.fake.FakeCardFlipPlayer
import com.wojdor.memolki.test.fake.FakeCardPairMatchedPlayer
import com.wojdor.memolki.test.fake.FakeCoinsPlayer
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import com.wojdor.memolki.test.fake.FakeDataStore
import com.wojdor.memolki.test.fake.FakeEncryptor
import com.wojdor.memolki.test.fake.FakeGameServices
import com.wojdor.memolki.test.fake.FakeGetDailyChallengeCardsUseCase
import com.wojdor.memolki.test.fake.FakeGetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.test.fake.FakeGetTodayDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import com.wojdor.memolki.test.fake.FakeHasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeInAppReviewer
import com.wojdor.memolki.test.fake.FakeLevelCompletePlayer
import com.wojdor.memolki.test.fake.FakeLocalEncryptorKeyStore
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePackageNameProvider
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.test.fake.FakePushNotificationProvider
import com.wojdor.memolki.test.fake.FakeSaveDailyChallengeUseCase
import com.wojdor.memolki.test.fake.FakeStringProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
val testKoinModule = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineDispatcher>(DefaultDispatcher) { get<CoroutineDispatcher>() }
    single<CoroutineDispatcher>(MainDispatcher) { get<CoroutineDispatcher>() }
    single { SavedStateHandle() }
    single { Random(0) }

    singleOf(::FakeDataStore) { bind<DataStore<Preferences>>() }
    singleOf(::FakeDailyChallengeDao) { bind<DailyChallengeDao>() }

    singleOf(::FakeEncryptor) { bind<Encryptor>() }
    singleOf(::FakeLocalEncryptorKeyStore) { bind<LocalEncryptorKeyStore>() }

    singleOf(::FakeAllCardPairsDataSource) { bind<AllCardPairsDataSource>() }

    singleOf(::FakeAnalytics) { bind<Analytics>() }

    singleOf(::FakeTimeProvider) { bind<TimeProvider>() }
    singleOf(::FakeStringProvider) { bind<StringProvider>() }
    singleOf(::FakeAppForegroundProvider) { bind<AppForegroundProvider>() }
    singleOf(::FakeAppInstalledProvider) { bind<AppInstalledProvider>() }
    singleOf(::FakeLocaleProvider) { bind<LocaleProvider>() }
    singleOf(::FakePackageNameProvider) { bind<PackageNameProvider>() }
    singleOf(::FakePermissionProvider) { bind<PermissionProvider>() }
    singleOf(::FakePushNotificationProvider) { bind<PushNotificationProvider>() }

    singleOf(::FakeBillingHandler) { bind<BillingHandler>() }
    singleOf(::FakeGameServices) { bind<GameServices>() }
    singleOf(::FakeInAppReviewer) { bind<InAppReviewer>() }

    singleOf(::FakeNotificationScheduler) { bind<NotificationScheduler>() }

    singleOf(::FakeHapticFeedback) { bind<HapticFeedback>() }
    singleOf(::FakeBackgroundMusicPlayer) { bind<BackgroundMusicPlayer>() }
    singleOf(::FakeCardFlipPlayer) { bind<CardFlipPlayer>() }
    singleOf(::FakeCardPairMatchedPlayer) { bind<CardPairMatchedPlayer>() }
    singleOf(::FakeCoinsPlayer) { bind<CoinsPlayer>() }
    singleOf(::FakeLevelCompletePlayer) { bind<LevelCompletePlayer>() }

    singleOf(::FakeAllRewardedAds) { bind<AllRewardedAds>() }
}
