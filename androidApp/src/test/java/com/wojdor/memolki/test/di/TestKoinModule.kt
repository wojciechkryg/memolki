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
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.data.local.datastore.settings.SettingsLocalDataSource
import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.DefaultDispatcher
import com.wojdor.memolki.di.MainDispatcher
import com.wojdor.memolki.domain.model.StarCalculator
import com.wojdor.memolki.domain.usecase.CalculateCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase
import com.wojdor.memolki.domain.usecase.CanUnlockNewCardUseCase
import com.wojdor.memolki.domain.usecase.ChangeLanguageUseCase
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCase
import com.wojdor.memolki.domain.usecase.CollectDailyStreakRewardUseCase
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetAllCardPairsUseCase
import com.wojdor.memolki.domain.usecase.GetAllDailyChallengesUseCase
import com.wojdor.memolki.domain.usecase.GetBiggestUnlockedBoardUseCase
import com.wojdor.memolki.domain.usecase.GetBoardsUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetCollectionDataUseCase
import com.wojdor.memolki.domain.usecase.GetCurrentLanguageTagUseCase
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetLanguagesWithCurrentUseCase
import com.wojdor.memolki.domain.usecase.GetLevelUseCase
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.domain.usecase.GetMoreAppsUseCase
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetSupportedLanguagesUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.domain.usecase.HasAnyDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasNotPlayedAnyGameUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.HasReceivedShareRewardUseCase
import com.wojdor.memolki.domain.usecase.IncrementLevelUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalCardPairsMatchedUseCase
import com.wojdor.memolki.domain.usecase.IncrementTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.IncrementUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.IsAppInstalledUseCase
import com.wojdor.memolki.domain.usecase.IsShopAdCooldownOverUseCase
import com.wojdor.memolki.domain.usecase.ObserveMusicEnabledUseCase
import com.wojdor.memolki.domain.usecase.ObserveSoundEnabledUseCase
import com.wojdor.memolki.domain.usecase.PrepareRecordingDataUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForBoardUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShareUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopPurchaseUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.ScheduleAdRewardNotificationUseCase
import com.wojdor.memolki.domain.usecase.SetLastShopAdShownTimestampUseCase
import com.wojdor.memolki.domain.usecase.ShouldShowNotificationRequestUseCase
import com.wojdor.memolki.domain.usecase.ToggleSettingsUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllCardPairsUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardUseCase
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
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.app.AppViewModel
import com.wojdor.memolki.ui.feature.cardpairdetails.CardPairDetailsViewModel
import com.wojdor.memolki.ui.feature.changelanguage.ChangeLanguageViewModel
import com.wojdor.memolki.ui.feature.chooseboard.ChooseBoardViewModel
import com.wojdor.memolki.ui.feature.collection.CollectionViewModel
import com.wojdor.memolki.ui.feature.dailychallengehistory.DailyChallengeHistoryViewModel
import com.wojdor.memolki.ui.feature.enablenotifications.EnableNotificationsViewModel
import com.wojdor.memolki.ui.feature.endgame.EndGameViewModel
import com.wojdor.memolki.ui.feature.game.GameViewModel
import com.wojdor.memolki.ui.feature.menu.MenuViewModel
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsViewModel
import com.wojdor.memolki.ui.feature.settings.SettingsViewModel
import com.wojdor.memolki.ui.feature.shop.ShopViewModel
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.formatter.CasualShareFormatter
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.formatter.TimeFormatter
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.TimeProvider
import io.mockk.every
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.androidx.viewmodel.dsl.viewModelOf
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
    single<ReviewManager> { relaxedMockk() }
    single<GooglePlayGames> { relaxedMockk() }
    single<FirebaseMessaging> { relaxedMockk() }
    single<Analytics> { relaxedMockk() }
    single<DailyChallengeDao> { relaxedMockk() }
    single<LocalEncryptorKeyStore> { relaxedMockk() }

    singleOf(::UnlockedCardPairsLocalDataSource)
    singleOf(::NotificationLocalDataSource)
    singleOf(::SettingsLocalDataSource)
    singleOf(::UserLocalDataSource)

    singleOf(::CardRepository)
    singleOf(::DailyChallengeRepository)
    singleOf(::NotificationRepository)
    singleOf(::SettingsRepository)
    singleOf(::UserRepository)

    factoryOf(::CasualShareFormatter)
    factoryOf(::DailyChallengeShareFormatter)
    factoryOf(::TimeFormatter)

    factoryOf(::StarCalculator)

    factoryOf(::CalculateCoinsForShopAdUseCase)
    factoryOf(::CanUnlockNewCardUseCase)
    factoryOf(::CheckDailyLoginStreakUseCase)
    factoryOf(::CollectDailyStreakRewardUseCase)
    factoryOf(::GetAllDailyChallengesUseCase)
    factoryOf(::GetCoinsUseCase)
    factoryOf(::GetCurrentLanguageTagUseCase)
    factoryOf(::GetDailyChallengeCardsUseCase)
    factoryOf(::GetLanguagesWithCurrentUseCase)
    factoryOf(::GetLevelUseCase)
    factoryOf(::GetShuffledUnlockedCardsUseCase)
    factoryOf(::GetTodayDailyChallengeUseCase)
    factoryOf(::GetTotalCardPairsMatchedUseCase)
    factoryOf(::GetTotalCoinsUseCase)
    factoryOf(::GetTotalGamesPlayedUseCase)
    factoryOf(::GetUnlockedCardPairsCountUseCase)
    factoryOf(::GetUnlockedCardPairsFromAdsCountUseCase)
    factoryOf(::GetUnlockedCardPairsUseCase)
    factoryOf(::HasAnyDailyChallengeUseCase)
    factoryOf(::HasNotPlayedAnyGameUseCase)
    factoryOf(::HasPlayedTodayDailyChallengeUseCase)
    factoryOf(::HasReceivedShareRewardUseCase)
    factoryOf(::IncrementLevelUseCase)
    factoryOf(::IncrementTotalCardPairsMatchedUseCase)
    factoryOf(::IncrementTotalGamesPlayedUseCase)
    factoryOf(::IncrementUnlockedCardPairsFromAdsCountUseCase)
    factoryOf(::IsShopAdCooldownOverUseCase)
    factoryOf(::PrepareRecordingDataUseCase)
    factoryOf(::RewardCoinsForBoardUseCase)
    factoryOf(::RewardCoinsForShareUseCase)
    factoryOf(::RewardCoinsForShopAdUseCase)
    factoryOf(::RewardCoinsForShopPurchaseUseCase)
    factoryOf(::SaveDailyChallengeUseCase)
    factoryOf(::ScheduleAdRewardNotificationUseCase)
    factoryOf(::SetLastShopAdShownTimestampUseCase)
    factoryOf(::ShouldShowNotificationRequestUseCase)
    factoryOf(::UnlockAllCardPairsUseCase)
    factoryOf(::UnlockAllNewCardPairsIfPurchasedUseCase)
    factoryOf(::UnlockRandomCardIfEnoughCoinsUseCase)
    factoryOf(::UnlockRandomCardUseCase)

    factory { CalculateNextCardPairCostUseCase(get(DefaultDispatcher), get(), get(), get()) }
    factory { GetAllCardPairsCountUseCase(get(DefaultDispatcher), get()) }
    factory { GetAllCardPairsUseCase(get(DefaultDispatcher), get()) }
    factory { GetBiggestUnlockedBoardUseCase(get(DefaultDispatcher), get()) }
    factory { GetBoardsUseCase(get(DefaultDispatcher), get()) }
    factory { GetCollectionDataUseCase(get(DefaultDispatcher), get(), get(), get(), get()) }
    factory { GetMenuUseCase(get(DefaultDispatcher)) }
    factory { GetMoreAppsUseCase(get(DefaultDispatcher), get()) }
    factory { GetSettingsUseCase(get(DefaultDispatcher), get()) }
    factory { GetSupportedLanguagesUseCase(get(DefaultDispatcher)) }
    factory { IsAppInstalledUseCase(get(DefaultDispatcher), get()) }
    factory { ObserveMusicEnabledUseCase(get(DefaultDispatcher), get()) }
    factory { ObserveSoundEnabledUseCase(get(DefaultDispatcher), get()) }
    factory { ToggleSettingsUseCase(get(DefaultDispatcher), get()) }
    factory { ChangeLanguageUseCase(get(MainDispatcher), get()) }

    viewModelOf(::AppViewModel)
    viewModelOf(::CardPairDetailsViewModel)
    viewModelOf(::ChangeLanguageViewModel)
    viewModelOf(::ChooseBoardViewModel)
    viewModelOf(::CollectionViewModel)
    viewModelOf(::DailyChallengeHistoryViewModel)
    viewModelOf(::EnableNotificationsViewModel)
    viewModelOf(::EndGameViewModel)
    viewModelOf(::GameViewModel)
    viewModelOf(::MenuViewModel)
    viewModelOf(::MoreAppsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ShopViewModel)
}
