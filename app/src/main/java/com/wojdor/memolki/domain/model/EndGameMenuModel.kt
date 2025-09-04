package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class EndGameMenuModel(@field:StringRes val textId: Int) : Parcelable {
    object PlayAgain : EndGameMenuModel(R.string.play_again)
    object Menu : EndGameMenuModel(R.string.menu)
}
