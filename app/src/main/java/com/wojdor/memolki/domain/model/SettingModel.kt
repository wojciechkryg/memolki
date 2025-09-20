package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class SettingModel(
    @field:StringRes val textId: Int,
    @field:DrawableRes val resId: Int,
) : Parcelable {
    abstract val isEnabled: Boolean

    data class Music(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_music, R.drawable.ic_settings_music)

    data class Sound(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_sound, R.drawable.ic_settings_sound)

    data class Vibration(override val isEnabled: Boolean = false) :
        SettingModel(R.string.setting_vibration, R.drawable.ic_settings_vibration)
}
