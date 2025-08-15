package com.wojdor.memolki.ui.feature.menu

import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetMenuUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenCollectionScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnNewGameClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenuUseCase: GetMenuUseCase
) : MviViewModel<MenuIntent, MenuState>(MenuState()) {

    init {
        loadMenu()
    }

    override fun onIntent(intent: MenuIntent) {
        when (intent) {
            OnNewGameClicked -> sendEffect(OpenChooseLevelScreen)
            OnCollectionClicked -> sendEffect(OpenCollectionScreen)
            OnSettingsClicked -> sendEffect(OpenSettingsScreen)
        }
    }

    private fun loadMenu() {
        getMenuUseCase().onEach {
            it.onSuccess { menu ->
                sendState { copy(menu = menu) }
            }
        }.launchIn(viewModelScope)
    }
}
