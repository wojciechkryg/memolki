package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

@Serializable
sealed class EndGameMenuModel(@field:StringRes val textId: Int = R.string.empty) {
    @Serializable
    object WatchAd : EndGameMenuModel()

    @Serializable
    object UnlockNewCard : EndGameMenuModel()

    @Serializable
    object Next : EndGameMenuModel(R.string.next)

    @Serializable
    object Menu : EndGameMenuModel(R.string.menu)

    @Serializable
    data class Share(
        val showReward: Boolean,
        val rewardCoins: Long = 0L
    ) : EndGameMenuModel()

    @Serializable
    object Compare : EndGameMenuModel(R.string.daily_challenge_compare)
}
