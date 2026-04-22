@file:UseSerializers(StringResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.collection
import com.wojdor.memolki.shared.resources.daily_reward
import com.wojdor.memolki.shared.resources.leaderboard
import com.wojdor.memolki.shared.resources.play
import com.wojdor.memolki.shared.resources.settings
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class MenuModel(
    val textId: StringResource
) {
    @Serializable
    object Play : MenuModel(Res.string.play)

    @Serializable
    object Collection : MenuModel(Res.string.collection)

    @Serializable
    object DailyReward : MenuModel(Res.string.daily_reward)

    @Serializable
    object Leaderboard : MenuModel(Res.string.leaderboard)

    @Serializable
    object Settings : MenuModel(Res.string.settings)
}
