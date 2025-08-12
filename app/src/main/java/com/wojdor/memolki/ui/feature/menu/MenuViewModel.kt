package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.feature.menu.MenuEffect.OpenSettingsScreen
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnCollectionClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnSettingsClicked
import com.wojdor.memolki.ui.feature.menu.MenuIntent.OnStartGameClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : MviViewModel<MenuIntent, MenuState>(MenuState()) {

    override fun onIntent(intent: MenuIntent) {
        when (intent) {
            OnStartGameClicked -> sendEffect(OpenChooseLevelScreen)
            OnCollectionClicked -> sendEffect(OpenSettingsScreen)
            OnSettingsClicked -> sendEffect(OpenSettingsScreen)
        }
    }
}
