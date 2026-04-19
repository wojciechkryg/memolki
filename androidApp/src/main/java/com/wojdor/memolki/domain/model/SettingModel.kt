package com.wojdor.memolki.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

// TODO(compose-resources): move to commonMain once Phase 13 lets us replace R.string.*/R.drawable.* in subclass super-constructors with Res.*
@Serializable
sealed class SettingModel(
    @field:StringRes val textId: Int,
    @field:DrawableRes val resId: Int,
) {
    abstract val isEnabled: Boolean

    @Serializable
    data class Music(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_music, R.drawable.ic_settings_music)

    @Serializable
    data class Sound(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_sound, R.drawable.ic_settings_sound)

    @Serializable
    data class Vibration(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_vibration, R.drawable.ic_settings_vibration)
}
