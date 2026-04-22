package com.wojdor.memolki.util.serializer

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.allDrawableResources
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.compose.resources.DrawableResource

object DrawableResourceSerializer : KSerializer<DrawableResource> {

    private val keyByResource: Map<DrawableResource, String> by lazy {
        Res.allDrawableResources.entries.associate { (key, value) -> value to key }
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DrawableResource", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DrawableResource) {
        encoder.encodeString(keyByResource[value].orEmpty())
    }

    override fun deserialize(decoder: Decoder): DrawableResource {
        val key = decoder.decodeString()
        return Res.allDrawableResources[key]
            ?: error("Unknown DrawableResource key: $key")
    }
}
