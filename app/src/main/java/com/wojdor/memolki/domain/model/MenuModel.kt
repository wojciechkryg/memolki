package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class MenuModel(@StringRes val textId: Int) : Parcelable {
    object NewGame : MenuModel(R.string.new_game)
    object Collection : MenuModel(R.string.collection)
    object Settings : MenuModel(R.string.settings)
}
