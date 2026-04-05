package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class EndGameMenuModel(@field:StringRes val textId: Int = R.string.empty) : Parcelable {
    object WatchAd : EndGameMenuModel()
    object UnlockNewCard : EndGameMenuModel()
    object FreeCoins : EndGameMenuModel(R.string.free_coins)
    object Continue : EndGameMenuModel(R.string.continue_game)
    object Menu : EndGameMenuModel(R.string.menu)
    data class Share(
        val showReward: Boolean,
        val rewardCoins: Long = 0L
    ) : EndGameMenuModel()

    object Compare : EndGameMenuModel(R.string.daily_challenge_compare)
}
