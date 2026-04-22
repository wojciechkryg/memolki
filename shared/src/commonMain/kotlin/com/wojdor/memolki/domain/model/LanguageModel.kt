package com.wojdor.memolki.domain.model

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.empty
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
data class LanguageModel(
    @Serializable(with = StringResourceSerializer::class)
    val textId: StringResource = Res.string.empty,
    val tag: String = ""
)
