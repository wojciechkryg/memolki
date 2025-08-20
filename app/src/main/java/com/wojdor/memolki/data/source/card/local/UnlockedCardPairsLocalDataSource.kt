package com.wojdor.memolki.data.source.card.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class UnlockedCardPairsLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val allCardPairsDataSource: AllCardPairsDataSource
) {

    fun getUnlockedCardPairIds(): List<String> {
        val defaultCardPairIds = allCardPairsDataSource.getAllCardPairs()
            .take(DEFAULT_UNLOCKED_CARD_PAIRS_COUNT)
            .map { it.id }
            .toSet()
        val unlockedCardPairIds =
            sharedPreferences.getStringSet(UNLOCKED_CARD_PAIRS_KEY, defaultCardPairIds)
        sharedPreferences.edit { putStringSet(UNLOCKED_CARD_PAIRS_KEY, unlockedCardPairIds) }
        return unlockedCardPairIds?.toList() ?: defaultCardPairIds.toList()
    }

    fun addUnlockedCardPairId(unlockedCardPairId: String) {
        val unlockedCardPairIds = getUnlockedCardPairIds().toMutableSet()
        unlockedCardPairIds.add(unlockedCardPairId)
        sharedPreferences.edit { putStringSet(UNLOCKED_CARD_PAIRS_KEY, unlockedCardPairIds) }
    }

    companion object {
        private const val DEFAULT_UNLOCKED_CARD_PAIRS_COUNT = 10

        private const val UNLOCKED_CARD_PAIRS_KEY = "unlocked_card_pairs"
    }
}
