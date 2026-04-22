package com.wojdor.memolki.util.serializer

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.allStringResources
import com.wojdor.memolki.shared.resources.empty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.compose.resources.StringResource

object StringResourceSerializer : KSerializer<StringResource> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringResource", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: StringResource) {
        encoder.encodeString(value.key)
    }

    override fun deserialize(decoder: Decoder): StringResource {
        val key = decoder.decodeString()
        return Res.allStringResources[key] ?: Res.string.empty
    }
}
