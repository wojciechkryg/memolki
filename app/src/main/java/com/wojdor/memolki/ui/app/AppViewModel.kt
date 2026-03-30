package com.wojdor.memolki.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.domain.usecase.PrepareRecordingCoinsUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import com.wojdor.memolki.ui.component.RECORDING_MODE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val unlockAllNewCardPairsIfPurchasedUseCase: UnlockAllNewCardPairsIfPurchasedUseCase,
    private val localEncryptorKeyStore: LocalEncryptorKeyStore,
    private val prepareRecordingCoinsUseCase: PrepareRecordingCoinsUseCase
) : ViewModel() {

    fun unlockAllNewCardPairsIfPurchased() {
        viewModelScope.launch {
            localEncryptorKeyStore.getSecretKey()
            if (RECORDING_MODE) prepareRecordingCoinsUseCase().collect()
            unlockAllNewCardPairsIfPurchasedUseCase().collect()
        }
    }
}
