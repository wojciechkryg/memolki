package com.wojdor.memolki.test.mock

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.source.card.local.AllCardPairsDataSource

object MockAllCardPairsDataSource : AllCardPairsDataSource {

    override fun getAllCardPairs() = listOf(
        CardPairEntity(
            "banana",
            CardEntity.Image(1, 1) to
                    CardEntity.Image(1, 1)
        ),
        CardPairEntity(
            "apple",
            CardEntity.Image(2, 2) to
                    CardEntity.Text(2)
        ),
        CardPairEntity(
            "strawberry",
            CardEntity.Text(3) to
                    CardEntity.Text(3)
        ),
        CardPairEntity(
            "orange",
            CardEntity.Text(4) to
                    CardEntity.Text(4)
        ),
        CardPairEntity(
            "grape",
            CardEntity.Text(5) to
                    CardEntity.Text(5)
        ),
        CardPairEntity(
            "watermelon",
            CardEntity.Text(6) to
                    CardEntity.Text(6)
        ),
        CardPairEntity(
            "mango",
            CardEntity.Text(7) to
                    CardEntity.Text(7)
        ),
        CardPairEntity(
            "peach",
            CardEntity.Text(8) to
                    CardEntity.Text(8)
        ),
        CardPairEntity(
            "pineapple",
            CardEntity.Text(9) to
                    CardEntity.Text(9)
        ),
        CardPairEntity(
            "blueberry",
            CardEntity.Text(10) to
                    CardEntity.Text(10)
        )
    )
}
