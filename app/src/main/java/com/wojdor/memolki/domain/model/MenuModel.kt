package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class MenuModel(@field:StringRes val textId: Int) : Parcelable {
    object Play : MenuModel(R.string.play)
    object Collection : MenuModel(R.string.collection)
    object DailyReward : MenuModel(R.string.daily_reward)
    object Leaderboard : MenuModel(R.string.leaderboard)
    object Settings : MenuModel(R.string.settings)
}
