package com.wojdor.memolki.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LanguageModel(
    // TODO(compose-resources): replace 0 default with Res.string.empty once Phase 13 lands.
    val textId: Int = 0,
    val tag: String = ""
)
