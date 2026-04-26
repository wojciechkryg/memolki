package com.wojdor.memolki.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.google.firebase.messaging.FirebaseMessaging
import com.wojdor.memolki.data.crypto.AndroidLocalEncryptorKeyStore
import com.wojdor.memolki.data.crypto.AndroidEncryptor
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.card.AllCardPairsLocalDataSource
import com.wojdor.memolki.data.local.database.AppDatabase
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.databaseBuilder
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.ui.ads.AdsInitializer
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.AndroidAllRewardedAds
import com.wojdor.memolki.util.AndroidAppOpener
import com.wojdor.memolki.util.AppOpener
import com.wojdor.memolki.util.billing.AndroidBillingHandler
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.extension.AndroidTextSharer
import com.wojdor.memolki.util.extension.AndroidToaster
import com.wojdor.memolki.util.extension.TextSharer
import com.wojdor.memolki.util.extension.Toaster
import com.wojdor.memolki.util.formatter.AndroidEpochDayFormatter
import com.wojdor.memolki.util.formatter.EpochDayFormatter
import com.wojdor.memolki.util.gameservices.AndroidGameServices
import com.wojdor.memolki.util.gameservices.GameServices
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
import com.wojdor.memolki.util.provider.ActivityProvider
import com.wojdor.memolki.util.provider.AndroidPushNotificationProvider
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import com.wojdor.memolki.util.review.AndroidInAppReviewer
import com.wojdor.memolki.util.review.InAppReviewer
import com.wojdor.memolki.util.update.InAppUpdate
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.random.Random

const val DATA_STORE_NAME = "memolki_preferences"
private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

val appKoinModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<CoroutineDispatcher>(DefaultDispatcher) { Dispatchers.Default }
    single<CoroutineDispatcher>(MainDispatcher) { Dispatchers.Main }
    single { if (RECORDING_MODE) Random(0) else Random.Default }
    single { Firebase.analytics }
    single { FirebaseMessaging.getInstance() }
    single<DataStore<Preferences>> { get<Context>().dataStore }
    single {
        databaseBuilder(get<Context>())
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().dailyChallengeDao() }
    singleOf(::AndroidEncryptor) { bind<Encryptor>() }
    singleOf(::AndroidLocalEncryptorKeyStore) { bind<LocalEncryptorKeyStore>() }
    singleOf(::AllCardPairsLocalDataSource) { bind<AllCardPairsDataSource>() }
    singleOf(::ActivityProvider)
    singleOf(::AppForegroundProvider)
    singleOf(::AppInstalledProvider)
    singleOf(::LocaleProvider)
    singleOf(::PackageNameProvider)
    singleOf(::PermissionProvider)
    singleOf(::AndroidPushNotificationProvider) { bind<PushNotificationProvider>() }
    singleOf(::AndroidBillingHandler) { bind<BillingHandler>() }
    singleOf(::AndroidGameServices) { bind<GameServices>() }
    singleOf(::AndroidInAppReviewer) { bind<InAppReviewer>() }
    singleOf(::AndroidTextSharer) { bind<TextSharer>() }
    singleOf(::AndroidToaster) { bind<Toaster>() }
    singleOf(::AndroidAppOpener) { bind<AppOpener>() }
    singleOf(::AndroidEpochDayFormatter) { bind<EpochDayFormatter>() }
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
}
