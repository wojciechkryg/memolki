package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.R
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import javax.inject.Inject

class AllCardPairsLocalDataSource @Inject constructor() : AllCardPairsDataSource {

    override fun getAllCardPairs(): LinkedHashSet<CardPairEntity> = linkedSetOf(
        CardPairEntity(
            id = "banana",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.banana,
                    imageRes = R.drawable.img_banana_whole
                ),
                CardEntity.Image(
                    textRes = R.string.banana,
                    imageRes = R.drawable.img_banana_half
                )
            )
        ),
        CardPairEntity(
            id = "apple",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.apple,
                    imageRes = R.drawable.img_apple_whole
                ),
                CardEntity.Image(
                    textRes = R.string.apple,
                    imageRes = R.drawable.img_apple_half
                )
            )
        ),
        CardPairEntity(
            id = "strawberry",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.strawberry,
                    imageRes = R.drawable.img_strawberry_whole
                ),
                CardEntity.Image(
                    textRes = R.string.strawberry,
                    imageRes = R.drawable.img_strawberry_half
                )
            )
        ),
        CardPairEntity(
            id = "orange",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.orange,
                    imageRes = R.drawable.img_orange_whole
                ),
                CardEntity.Image(
                    textRes = R.string.orange,
                    imageRes = R.drawable.img_orange_half
                )
            )
        ),
        CardPairEntity(
            id = "grape",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.grape,
                    imageRes = R.drawable.img_grape_whole
                ),
                CardEntity.Image(
                    textRes = R.string.grape,
                    imageRes = R.drawable.img_grape_half
                )
            )
        ),
        CardPairEntity(
            id = "watermelon",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.watermelon,
                    imageRes = R.drawable.img_watermelon_whole
                ),
                CardEntity.Image(
                    textRes = R.string.watermelon,
                    imageRes = R.drawable.img_watermelon_half
                )
            )
        ),
        CardPairEntity(
            id = "mango",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.mango,
                    imageRes = R.drawable.img_mango_whole
                ),
                CardEntity.Image(
                    textRes = R.string.mango,
                    imageRes = R.drawable.img_mango_half
                )
            )
        ),
        CardPairEntity(
            id = "peach",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.peach,
                    imageRes = R.drawable.img_peach_whole
                ),
                CardEntity.Image(
                    textRes = R.string.peach,
                    imageRes = R.drawable.img_peach_half
                )
            )
        ),
        CardPairEntity(
            id = "pineapple",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.pineapple,
                    imageRes = R.drawable.img_pineapple_whole
                ),
                CardEntity.Image(
                    textRes = R.string.pineapple,
                    imageRes = R.drawable.img_pineapple_half
                )
            )
        ),
        CardPairEntity(
            id = "blueberry",
            pair = Pair(
                CardEntity.Image(
                    textRes = R.string.blueberry,
                    imageRes = R.drawable.img_blueberry_whole
                ),
                CardEntity.Image(
                    textRes = R.string.blueberry,
                    imageRes = R.drawable.img_blueberry_half
                )
            )
        ),
    )
}
