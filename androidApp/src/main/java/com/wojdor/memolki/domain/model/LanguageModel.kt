package com.wojdor.memolki.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageModel(
    @field:StringRes val textId: Int = R.string.empty,
    val tag: String = ""
) : Parcelable
