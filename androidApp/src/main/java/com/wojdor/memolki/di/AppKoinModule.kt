package com.wojdor.memolki.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wojdor.memolki.data.local.database.databaseBuilder
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.messaging.FirebaseMessaging
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import com.wojdor.memolki.data.crypto.BaseEncryptor
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.card.AllCardPairsLocalDataSource
import com.wojdor.memolki.data.local.database.AppDatabase
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
import com.wojdor.memolki.ui.ads.AdsInitializer
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.AndroidAllRewardedAds
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
import com.wojdor.memolki.util.billing.AndroidBillingHandler
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.formatter.CasualShareFormatter
import com.wojdor.memolki.util.formatter.DailyChallengeShareFormatter
import com.wojdor.memolki.util.formatter.TimeFormatter
import com.wojdor.memolki.util.resource.AppStringProvider
import com.wojdor.memolki.util.resource.StringProvider
import com.wojdor.memolki.util.media.AndroidBackgroundMusicPlayer
import com.wojdor.memolki.util.media.AndroidCardFlipPlayer
import com.wojdor.memolki.util.media.AndroidCardPairMatchedPlayer
import com.wojdor.memolki.util.media.AndroidCoinsPlayer
import com.wojdor.memolki.util.media.AndroidHapticFeedback
import com.wojdor.memolki.util.media.AndroidLevelCompletePlayer
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler
import com.wojdor.memolki.util.notification.NotificationCreator
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.AndroidGooglePlayGames
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import com.wojdor.memolki.util.provider.TimeProvider
import com.wojdor.memolki.util.update.InAppUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.random.Random

val DefaultDispatcher = named("default")
val MainDispatcher = named("main")

const val DATA_STORE_NAME = "memolki_preferences"
private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

val appKoinModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<CoroutineDispatcher>(DefaultDispatcher) { Dispatchers.Default }
    single<CoroutineDispatcher>(MainDispatcher) { Dispatchers.Main }
    single { if (RECORDING_MODE) Random(0) else Random.Default }
    single { Firebase.analytics }
    single { FirebaseMessaging.getInstance() }
    single<ReviewManager> { ReviewManagerFactory.create(get()) }
    single<DataStore<Preferences>> { get<Context>().dataStore }
    single {
        databaseBuilder(get<Context>())
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().dailyChallengeDao() }

    singleOf(::BaseEncryptor) { bind<Encryptor>() }
    singleOf(::LocalEncryptorKeyStore)

    singleOf(::AllCardPairsLocalDataSource) { bind<AllCardPairsDataSource>() }
    singleOf(::UnlockedCardPairsLocalDataSource)
    singleOf(::NotificationLocalDataSource)
    singleOf(::SettingsLocalDataSource)
    singleOf(::UserLocalDataSource)

    singleOf(::CardRepository)
    singleOf(::DailyChallengeRepository)
    singleOf(::NotificationRepository)
    singleOf(::SettingsRepository)
    singleOf(::UserRepository)

    singleOf(::AppForegroundProvider)
    singleOf(::AppInstalledProvider)
    singleOf(::LocaleProvider)
    singleOf(::PackageNameProvider)
    singleOf(::PermissionProvider)
    singleOf(::PushNotificationProvider)
    singleOf(::TimeProvider)

    singleOf(::Analytics)
    singleOf(::AndroidBillingHandler) { bind<BillingHandler>() }
    singleOf(::AndroidGooglePlayGames) { bind<GooglePlayGames>() }
    singleOf(::InAppUpdate)
    singleOf(::NotificationCreator)
    singleOf(::AndroidNotificationScheduler) { bind<NotificationScheduler>() }
    singleOf(::AndroidHapticFeedback) { bind<HapticFeedback>() }
    singleOf(::AndroidBackgroundMusicPlayer) { bind<BackgroundMusicPlayer>() }
    singleOf(::AdsInitializer)
    singleOf(::AndroidAllRewardedAds) { bind<AllRewardedAds>() }
    single<CardFlipPlayer> { AndroidCardFlipPlayer(get(), get(MainDispatcher), get()) }
    single<CardPairMatchedPlayer> { AndroidCardPairMatchedPlayer(get(), get(MainDispatcher), get()) }
    single<CoinsPlayer> { AndroidCoinsPlayer(get(), get(MainDispatcher), get()) }
    single<LevelCompletePlayer> { AndroidLevelCompletePlayer(get(), get(MainDispatcher), get()) }

    singleOf(::AppStringProvider) { bind<StringProvider>() }
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

