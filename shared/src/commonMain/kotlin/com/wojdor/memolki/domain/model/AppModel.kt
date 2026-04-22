@file:UseSerializers(
    StringResourceSerializer::class,
    DrawableResourceSerializer::class,
    ColorSerializer::class
)

package com.wojdor.memolki.domain.model

import androidx.compose.ui.graphics.Color
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.ic_logo_bird_side
import com.wojdor.memolki.shared.resources.ic_logo_fruit_half
import com.wojdor.memolki.shared.resources.ic_logo_mammal_side
import com.wojdor.memolki.shared.resources.ic_logo_vegetable_half
import com.wojdor.memolki.shared.resources.suffix_bird_side
import com.wojdor.memolki.shared.resources.suffix_fruit_half
import com.wojdor.memolki.shared.resources.suffix_mammal_side
import com.wojdor.memolki.shared.resources.suffix_vegetable_half
import com.wojdor.memolki.util.serializer.ColorSerializer
import com.wojdor.memolki.util.serializer.DrawableResourceSerializer
import com.wojdor.memolki.util.serializer.StringResourceSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed class AppModel {

    abstract val imageRes: DrawableResource
    abstract val textRes: StringResource
    abstract val color: Color
    abstract val appId: String

    companion object {

        fun all() = listOf(
            FruitHalf,
            VegetableHalf,
            MammalSide,
            BirdSide
        )
    }

    @Serializable
    object FruitHalf : AppModel() {
        override val imageRes: DrawableResource = Res.drawable.ic_logo_fruit_half
        override val textRes: StringResource = Res.string.suffix_fruit_half
        override val color: Color = Color(0xFFFFEAA1)
        override val appId: String = "com.wojdor.memolki.fruithalf"
    }

    @Serializable
    object VegetableHalf : AppModel() {
        override val imageRes: DrawableResource = Res.drawable.ic_logo_vegetable_half
        override val textRes: StringResource = Res.string.suffix_vegetable_half
        override val color: Color = Color(0xFFE6A0A0)
        override val appId: String = "com.wojdor.memolki.vegetablehalf"
    }

    @Serializable
    object MammalSide : AppModel() {
        override val imageRes: DrawableResource = Res.drawable.ic_logo_mammal_side
        override val textRes: StringResource = Res.string.suffix_mammal_side
        override val color: Color = Color(0xFFE2BA8B)
        override val appId: String = "com.wojdor.memolki.mammalside"
    }

    @Serializable
    object BirdSide : AppModel() {
        override val imageRes: DrawableResource = Res.drawable.ic_logo_bird_side
        override val textRes: StringResource = Res.string.suffix_bird_side
        override val color: Color = Color(0xFFB1DBE7)
        override val appId: String = "com.wojdor.memolki.birdside"
    }
}
