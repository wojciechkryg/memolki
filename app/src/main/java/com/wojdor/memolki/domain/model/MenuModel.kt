package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R

sealed class MenuModel(@StringRes val textId: Int) {
    object NewGame : MenuModel(R.string.new_game)
    object Collection : MenuModel(R.string.collection)
    object Settings : MenuModel(R.string.settings)
}
