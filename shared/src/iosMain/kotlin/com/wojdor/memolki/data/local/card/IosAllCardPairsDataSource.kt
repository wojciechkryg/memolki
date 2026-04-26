package com.wojdor.memolki.data.local.card

import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource

// TODO(kmp-ios): provide a per-flavor card-pair catalog when iOS resources ship.
// On Android the equivalent lives under androidApp/src/<flavor>/.
class IosAllCardPairsDataSource : AllCardPairsDataSource {
    override fun getAllCardPairs(): List<CardPairEntity> = emptyList()
}
