package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.img_test_half
import com.wojdor.memolki.shared.resources.img_test_whole
import com.wojdor.memolki.shared.resources.test_half
import com.wojdor.memolki.shared.resources.test_whole

fun getCollectionStateForPreview() = CollectionState(
    coins = 1234,
    collectionCardPairs = getCollectionCardPairsForPreview()
)

private fun getCollectionCardPairsForPreview() = listOf(
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("banana_whole", "banana", Res.string.test_whole, Res.drawable.img_test_whole),
            CardModel.Image("banana_half", "banana", Res.string.test_half, Res.drawable.img_test_half)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Image("apple_whole", "apple", Res.string.test_whole, Res.drawable.img_test_whole),
            CardModel.Text("apple_half", "apple", Res.string.test_half)
        )
    ),
    CollectionCardPairModel.Unlocked(
        CardPairModel(
            CardModel.Text("strawberry_whole", "strawberry", Res.string.test_whole),
            CardModel.Text("strawberry_half", "strawberry", Res.string.test_half)
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
