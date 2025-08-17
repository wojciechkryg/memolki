package com.wojdor.memolki

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.source.card.local.CardPairsLocalDataSource
import io.mockk.every

val mockCardPairsLocalDataSource = relaxedMockk<CardPairsLocalDataSource> {
    every { getCardPairs() } returns listOf(
        CardPairEntity(
            "banana",
            CardEntity.Image(123, 321) to
                    CardEntity.Image(123, 321)
        ),
        CardPairEntity(
            "apple",
            CardEntity.Image(456, 654) to
                    CardEntity.Text(456)
        ),
        CardPairEntity(
            "orange",
            CardEntity.Text(789) to
                    CardEntity.Text(789)
        )
    )
}
