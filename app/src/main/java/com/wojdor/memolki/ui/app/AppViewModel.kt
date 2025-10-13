package com.wojdor.memolki.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val unlockAllNewCardPairsIfPurchasedUseCase: UnlockAllNewCardPairsIfPurchasedUseCase
) : ViewModel() {

    fun unlockAllNewCardPairsIfPurchased() {
        unlockAllNewCardPairsIfPurchasedUseCase().launchIn(viewModelScope)
    }
}
