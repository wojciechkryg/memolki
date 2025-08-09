package com.wojdor.memolki.ui.menu

import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.menu.MenuEffect.OpenChooseLevelScreen
import com.wojdor.memolki.ui.menu.MenuEffect.OpenOptionsScreen
import com.wojdor.memolki.ui.menu.MenuIntent.OnStartGameClicked
import com.wojdor.memolki.ui.menu.MenuIntent.OnOptionsClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : MviViewModel<MenuIntent, MenuState>(MenuState()) {

    override fun onIntent(intent: MenuIntent) {
        when (intent) {
            OnStartGameClicked -> sendEffect(OpenChooseLevelScreen)
            OnOptionsClicked -> sendEffect(OpenOptionsScreen)
        }
    }
}
