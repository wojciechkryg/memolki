package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_HALF
import com.wojdor.memolki.ui.component.PREVIEW_IMAGE_WHOLE

fun getCollectionStateForPreview() = CollectionState(
    coins = 1234,
    collectionCardPairs = getCollectionCardPairsForPreview()
)

private fun getCollectionCardPairsForPreview() = listOf(
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("banana_whole", "banana", Res.string.banana, PREVIEW_IMAGE_WHOLE),
            CardModel.Image("banana_half", "banana", Res.string.banana, PREVIEW_IMAGE_HALF)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("apple_whole", "apple", Res.string.apple, PREVIEW_IMAGE_WHOLE),
            CardModel.Text("apple_half", "apple", Res.string.apple)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Text("strawberry_whole", "strawberry", Res.string.strawberry),
            CardModel.Text("strawberry_half", "strawberry", Res.string.strawberry)
        )
    ),
    CollectionCardPairModel.LockedToUnlockWithCoins(100),
    CollectionCardPairModel.LockedToUnlockWithAd,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked,
    CollectionCardPairModel.Locked
)
