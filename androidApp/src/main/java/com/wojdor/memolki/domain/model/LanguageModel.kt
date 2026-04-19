package com.wojdor.memolki.domain.model

import androidx.annotation.StringRes
import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

@Serializable
data class LanguageModel(
    @field:StringRes val textId: Int = R.string.empty,
    val tag: String = ""
)
