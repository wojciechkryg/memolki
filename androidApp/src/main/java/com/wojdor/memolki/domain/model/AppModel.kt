package com.wojdor.memolki.domain.model

import com.wojdor.memolki.R
import kotlinx.serialization.Serializable

@Serializable
sealed class AppModel {

    abstract val imageRes: Int
    abstract val textRes: Int
    abstract val colorRes: Int
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
        override val imageRes: Int = R.drawable.ic_logo_fruit_half
        override val textRes: Int = R.string.suffix_fruit_half
        override val colorRes: Int = R.color.primary_fruit_half
        override val appId: String = "com.wojdor.memolki.fruithalf"
    }

    @Serializable
    object VegetableHalf : AppModel() {
        override val imageRes: Int = R.drawable.ic_logo_vegetable_half
        override val textRes: Int = R.string.suffix_vegetable_half
        override val colorRes: Int = R.color.primary_vegetable_half
        override val appId: String = "com.wojdor.memolki.vegetablehalf"
    }

    @Serializable
    object MammalSide : AppModel() {
        override val imageRes: Int = R.drawable.ic_logo_mammal_side
        override val textRes: Int = R.string.suffix_mammal_side
        override val colorRes: Int = R.color.primary_mammal_side
        override val appId: String = "com.wojdor.memolki.mammalside"
    }

    @Serializable
    object BirdSide : AppModel() {
        override val imageRes: Int = R.drawable.ic_logo_bird_side
        override val textRes: Int = R.string.suffix_bird_side
        override val colorRes: Int = R.color.primary_bird_side
        override val appId: String = "com.wojdor.memolki.birdside"
    }
}
