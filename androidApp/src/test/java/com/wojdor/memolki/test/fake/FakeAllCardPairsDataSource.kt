package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.blueberry
import com.wojdor.memolki.shared.resources.grape
import com.wojdor.memolki.shared.resources.mango
import com.wojdor.memolki.shared.resources.orange
import com.wojdor.memolki.shared.resources.peach
import com.wojdor.memolki.shared.resources.pineapple
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.shared.resources.watermelon

class FakeAllCardPairsDataSource : AllCardPairsDataSource {

    var addedEpochDayOverrides: Map<String, Long> = emptyMap()

    override fun getAllCardPairs() = baseCardPairs().map { entity ->
        val override = addedEpochDayOverrides[entity.id]
        if (override != null) entity.copy(addedEpochDay = override) else entity
    }

    private fun baseCardPairs() = listOf(
        CardPairEntity(
            "banana",
            CardEntity.Image("banana_whole", Res.string.banana, 1) to
                    CardEntity.Image("banana_half", Res.string.banana, 1)
        ),
        CardPairEntity(
            "apple",
            CardEntity.Image("apple_whole", Res.string.apple, 2) to
                    CardEntity.Text("apple_half", Res.string.apple)
        ),
        CardPairEntity(
            "strawberry",
            CardEntity.Text("strawberry_whole", Res.string.strawberry) to
                    CardEntity.Text("strawberry_half", Res.string.strawberry)
        ),
        CardPairEntity(
            "orange",
            CardEntity.Text("orange_whole", Res.string.orange) to
                    CardEntity.Text("orange_half", Res.string.orange)
        ),
        CardPairEntity(
            "grape",
            CardEntity.Text("grape_whole", Res.string.grape) to
                    CardEntity.Text("grape_half", Res.string.grape)
        ),
        CardPairEntity(
            "watermelon",
            CardEntity.Text("watermelon_whole", Res.string.watermelon) to
                    CardEntity.Text("watermelon_half", Res.string.watermelon)
        ),
        CardPairEntity(
            "mango",
            CardEntity.Text("mango_whole", Res.string.mango) to
                    CardEntity.Text("mango_half", Res.string.mango)
        ),
        CardPairEntity(
            "peach",
            CardEntity.Text("peach_whole", Res.string.peach) to
                    CardEntity.Text("peach_half", Res.string.peach)
        ),
        CardPairEntity(
            "pineapple",
            CardEntity.Text("pineapple_whole", Res.string.pineapple) to
                    CardEntity.Text("pineapple_half", Res.string.pineapple)
        ),
        CardPairEntity(
            "blueberry",
            CardEntity.Text("blueberry_whole", Res.string.blueberry) to
                    CardEntity.Text("blueberry_half", Res.string.blueberry)
        )
    )
}
