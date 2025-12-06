package com.wojdor.memolki.domain.model

import android.os.Parcelable
import com.wojdor.memolki.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AppModel : Parcelable {

    abstract val imageRes: Int
    abstract val textRes: Int
    abstract val colorRes: Int
    abstract val appId: String

    companion object {

        fun all() = listOf(
            FruitHalf,
            VegetableHalf,
            MammalSide
        )
    }

    object FruitHalf : AppModel() {
        @IgnoredOnParcel
        override val imageRes: Int = R.drawable.ic_logo_fruit_half

        @IgnoredOnParcel
        override val textRes: Int = R.string.suffix_fruit_half

        @IgnoredOnParcel
        override val colorRes: Int = R.color.fruit_half_primary

        @IgnoredOnParcel
        override val appId: String = "com.wojdor.memolki.fruithalf"
    }

    object VegetableHalf : AppModel() {
        @IgnoredOnParcel
        override val imageRes: Int = R.drawable.ic_logo_vegetable_half

        @IgnoredOnParcel
        override val textRes: Int = R.string.suffix_vegetable_half

        @IgnoredOnParcel
        override val colorRes: Int = R.color.vegetable_half_primary

        @IgnoredOnParcel
        override val appId: String = "com.wojdor.memolki.vegetablehalf"
    }

    object MammalSide : AppModel() {
        @IgnoredOnParcel
        override val imageRes: Int = R.drawable.ic_logo_mammal_side

        @IgnoredOnParcel
        override val textRes: Int = R.string.suffix_mammal_side

        @IgnoredOnParcel
        override val colorRes: Int = R.color.mammal_side_primary

        @IgnoredOnParcel
        override val appId: String = "com.wojdor.memolki.mammalside"
    }
}
