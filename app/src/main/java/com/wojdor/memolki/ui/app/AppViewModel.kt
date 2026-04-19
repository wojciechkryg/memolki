package com.wojdor.memolki.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.PrepareRecordingDataUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val analytics: Analytics,
    private val unlockAllNewCardPairsIfPurchasedUseCase: UnlockAllNewCardPairsIfPurchasedUseCase,
    private val localEncryptorKeyStore: LocalEncryptorKeyStore,
    private val prepareRecordingDataUseCase: PrepareRecordingDataUseCase,
    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase,
    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase,
    private val localeProvider: LocaleProvider,
    private val permissionProvider: PermissionProvider,
    private val pushNotificationProvider: PushNotificationProvider,
    private val billingHandler: BillingHandler
) : ViewModel() {

    fun onAppCreate() {
        viewModelScope.launch {
            billingHandler.ensureConnected()
            pushNotificationProvider.subscribeToTopics()
            analytics.setUserLanguage(localeProvider.getLanguageTag())
            analytics.setNotificationPermission(permissionProvider.hasNotificationPermission())
            val totalGamesPlayed = getTotalGamesPlayedUseCase().first().getOrDefault(0L)
            val unlockedCardsCount = getUnlockedCardPairsCountUseCase().first().getOrDefault(0)
            analytics.logAppSessionStart(totalGamesPlayed, unlockedCardsCount)
            localEncryptorKeyStore.initialize()
            if (RECORDING_MODE) prepareRecordingDataUseCase().collect()
        }
        viewModelScope.launch {
            unlockAllNewCardPairsIfPurchasedUseCase().collect()
        }
    }

    fun onAppOpen(notificationType: String?, shortcutId: String? = null) {
        analytics.logAppOpened(notificationType, shortcutId)
    }

    suspend fun hasPlayedTodayDailyChallenge(): Boolean {
        return hasPlayedTodayDailyChallengeUseCase().first().getOrDefault(true)
    }
}
