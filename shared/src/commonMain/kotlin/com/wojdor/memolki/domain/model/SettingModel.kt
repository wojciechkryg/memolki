@file:UseSerializers(StringResourceSerializer::class, DrawableResourceSerializer::class)

package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.ic_settings_music
import com.wojdor.memolki.shared.resources.ic_settings_sound
import com.wojdor.memolki.shared.resources.ic_settings_vibration
import com.wojdor.memolki.shared.resources.setting_music
import com.wojdor.memolki.shared.resources.setting_sound
import com.wojdor.memolki.shared.resources.setting_vibration
import com.wojdor.memolki.util.serializer.DrawableResourceSerializer
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class SettingModel(
    val textId: StringResource,
    val resId: DrawableResource,
) {
    abstract val isEnabled: Boolean

    @Serializable
    data class Music(override val isEnabled: Boolean = false) :
        SettingModel(Res.string.setting_music, Res.drawable.ic_settings_music)

    @Serializable
    data class Sound(override val isEnabled: Boolean = false) :
        SettingModel(Res.string.setting_sound, Res.drawable.ic_settings_sound)

    @Serializable
    data class Vibration(override val isEnabled: Boolean = false) :
        SettingModel(Res.string.setting_vibration, Res.drawable.ic_settings_vibration)
}
