package com.wojdor.memolki.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val unlockAllNewCardPairsIfPurchasedUseCase: UnlockAllNewCardPairsIfPurchasedUseCase,
    private val localEncryptorKeyStore: LocalEncryptorKeyStore
) : ViewModel() {

    fun unlockAllNewCardPairsIfPurchased() {
        viewModelScope.launch {
            localEncryptorKeyStore.getSecretKey()
            unlockAllNewCardPairsIfPurchasedUseCase().collect()
        }
    }
}
