package com.wojdor.memolki.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LanguageModel(
    val textId: Int = 0,
    val tag: String = ""
)
