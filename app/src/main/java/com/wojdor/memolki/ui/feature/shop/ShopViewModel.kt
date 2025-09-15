package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : MviViewModel<ShopIntent, ShopState>(
    savedStateHandle,
    ShopState()
) {

    override fun onIntent(intent: ShopIntent) {
        when (intent) {
            else -> TODO()
        }
    }
}
