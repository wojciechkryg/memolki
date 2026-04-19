package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

@Serializable
sealed class MenuModel(@field:StringRes val textId: Int) {
    @Serializable
    object Play : MenuModel(R.string.play)

    @Serializable
    object Collection : MenuModel(R.string.collection)

    @Serializable
    object DailyReward : MenuModel(R.string.daily_reward)

    @Serializable
    object Leaderboard : MenuModel(R.string.leaderboard)

    @Serializable
    object Settings : MenuModel(R.string.settings)
}
