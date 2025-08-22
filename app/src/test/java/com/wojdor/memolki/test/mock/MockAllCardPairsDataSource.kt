package com.wojdor.memolki.test.mock

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.source.card.local.AllCardPairsDataSource

object MockAllCardPairsDataSource : AllCardPairsDataSource {

    override fun getAllCardPairs() = listOf(
        CardPairEntity(
            "banana",
            CardEntity.Image("banana_whole", 1, 1) to
                    CardEntity.Image("banana_half", 1, 1)
        ),
        CardPairEntity(
            "apple",
            CardEntity.Image("apple_whole", 2, 2) to
                    CardEntity.Text("apple_half", 2)
        ),
        CardPairEntity(
            "strawberry",
            CardEntity.Text("strawberry_whole", 3) to
                    CardEntity.Text("strawberry_half", 3)
        ),
        CardPairEntity(
            "orange",
            CardEntity.Text("orange_whole", 4) to
                    CardEntity.Text("orange_half", 4)
        ),
        CardPairEntity(
            "grape",
            CardEntity.Text("grape_whole", 5) to
                    CardEntity.Text("grape_half", 5)
        ),
        CardPairEntity(
            "watermelon",
            CardEntity.Text("watermelon_whole", 6) to
                    CardEntity.Text("watermelon_half", 6)
        ),
        CardPairEntity(
            "mango",
            CardEntity.Text("mango_whole", 7) to
                    CardEntity.Text("mango_half", 7)
        ),
        CardPairEntity(
            "peach",
            CardEntity.Text("peach_whole", 8) to
                    CardEntity.Text("peach_half", 8)
        ),
        CardPairEntity(
            "pineapple",
            CardEntity.Text("pineapple_whole", 9) to
                    CardEntity.Text("pineapple_half", 9)
        ),
        CardPairEntity(
            "blueberry",
            CardEntity.Text("blueberry_whole", 10) to
                    CardEntity.Text("blueberry_half", 10)
        )
    )
}
