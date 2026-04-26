package com.wojdor.memolki.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.crypto.IosEncryptor
import com.wojdor.memolki.data.crypto.IosLocalEncryptorKeyStore
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.card.IosAllCardPairsDataSource
import com.wojdor.memolki.data.local.database.AppDatabase
import com.wojdor.memolki.data.local.database.databaseBuilder
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.ads.IosAllRewardedAds
import com.wojdor.memolki.util.AppOpener
import com.wojdor.memolki.util.IosAppOpener
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.IosBillingHandler
import com.wojdor.memolki.util.extension.IosTextSharer
import com.wojdor.memolki.util.extension.IosToaster
import com.wojdor.memolki.util.extension.TextSharer
import com.wojdor.memolki.util.extension.Toaster
import com.wojdor.memolki.util.formatter.EpochDayFormatter
import com.wojdor.memolki.util.formatter.IosEpochDayFormatter
import com.wojdor.memolki.util.gameservices.GameServices
import com.wojdor.memolki.util.gameservices.IosGameServices
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.CardFlipPlayer
import com.wojdor.memolki.util.media.CardPairMatchedPlayer
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.media.IosBackgroundMusicPlayer
import com.wojdor.memolki.util.media.IosCardFlipPlayer
import com.wojdor.memolki.util.media.IosCardPairMatchedPlayer
import com.wojdor.memolki.util.media.IosCoinsPlayer
import com.wojdor.memolki.util.media.IosHapticFeedback
import com.wojdor.memolki.util.media.IosLevelCompletePlayer
import com.wojdor.memolki.util.media.LevelCompletePlayer
import com.wojdor.memolki.util.notification.IosNotificationScheduler
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.AppForegroundProvider
import com.wojdor.memolki.util.provider.AppInstalledProvider
import com.wojdor.memolki.util.provider.IosPushNotificationProvider
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.review.InAppReviewer
import com.wojdor.memolki.util.review.IosInAppReviewer
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import kotlin.random.Random

private const val DATA_STORE_FILE_NAME = "memolki_preferences.preferences_pb"

@OptIn(ExperimentalForeignApi::class)
private fun createIosDataStore(): DataStore<Preferences> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    val path = "${requireNotNull(documentDirectory?.path)}/$DATA_STORE_FILE_NAME"
    return PreferenceDataStoreFactory.createWithPath(produceFile = { path.toPath() })
}

val iosKoinModule = module {
    single<CoroutineDispatcher> { Dispatchers.Default }
    single<CoroutineDispatcher>(DefaultDispatcher) { Dispatchers.Default }
    single<CoroutineDispatcher>(MainDispatcher) { Dispatchers.Main }
    single { Random.Default }
    single { Firebase.analytics }
    single<DataStore<Preferences>> { createIosDataStore() }
    single {
        databaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().dailyChallengeDao() }

    singleOf(::IosEncryptor) { bind<Encryptor>() }
    singleOf(::IosLocalEncryptorKeyStore) { bind<LocalEncryptorKeyStore>() }

    singleOf(::IosAllCardPairsDataSource) { bind<AllCardPairsDataSource>() }

    singleOf(::AppForegroundProvider)
    singleOf(::AppInstalledProvider)
    singleOf(::LocaleProvider)
    singleOf(::PackageNameProvider)
    singleOf(::PermissionProvider)
    singleOf(::IosPushNotificationProvider) { bind<PushNotificationProvider>() }

    singleOf(::IosBillingHandler) { bind<BillingHandler>() }
    singleOf(::IosGameServices) { bind<GameServices>() }
    singleOf(::IosInAppReviewer) { bind<InAppReviewer>() }
    singleOf(::IosTextSharer) { bind<TextSharer>() }
    singleOf(::IosToaster) { bind<Toaster>() }
    singleOf(::IosAppOpener) { bind<AppOpener>() }
    singleOf(::IosEpochDayFormatter) { bind<EpochDayFormatter>() }
    singleOf(::IosNotificationScheduler) { bind<NotificationScheduler>() }
    singleOf(::IosHapticFeedback) { bind<HapticFeedback>() }
    singleOf(::IosBackgroundMusicPlayer) { bind<BackgroundMusicPlayer>() }
    singleOf(::IosAllRewardedAds) { bind<AllRewardedAds>() }
    singleOf(::IosCardFlipPlayer) { bind<CardFlipPlayer>() }
    singleOf(::IosCardPairMatchedPlayer) { bind<CardPairMatchedPlayer>() }
    singleOf(::IosCoinsPlayer) { bind<CoinsPlayer>() }
    singleOf(::IosLevelCompletePlayer) { bind<LevelCompletePlayer>() }
}
