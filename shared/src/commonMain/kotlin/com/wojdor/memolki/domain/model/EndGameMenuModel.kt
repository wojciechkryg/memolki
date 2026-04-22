@file:UseSerializers(StringResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.daily_challenge_compare
import com.wojdor.memolki.shared.resources.empty
import com.wojdor.memolki.shared.resources.menu
import com.wojdor.memolki.shared.resources.next
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class EndGameMenuModel(
    val textId: StringResource = Res.string.empty
) {
    @Serializable
    object WatchAd : EndGameMenuModel()

    @Serializable
    object UnlockNewCard : EndGameMenuModel()

    @Serializable
    object Next : EndGameMenuModel(Res.string.next)

    @Serializable
    object Menu : EndGameMenuModel(Res.string.menu)

    @Serializable
    data class Share(
        val showReward: Boolean,
        val rewardCoins: Long = 0L
    ) : EndGameMenuModel()

    @Serializable
    object Compare : EndGameMenuModel(Res.string.daily_challenge_compare)
}
