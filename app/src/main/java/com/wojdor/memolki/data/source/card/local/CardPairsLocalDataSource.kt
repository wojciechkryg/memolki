package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.R
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity

class CardPairsLocalDataSource {

    fun getCardPairs(): List<CardPairEntity> = listOf(
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
    )
}
