package com.wojdor.memolki.data.source.card.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.wojdor.memolki.util.extension.toLinkedSet
import javax.inject.Inject

class UnlockedCardPairsLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val allCardPairsDataSource: AllCardPairsDataSource
) {

    fun getUnlockedCardPairIds(): LinkedHashSet<String> {
        return (sharedPreferences.getStringSet(UNLOCKED_CARD_PAIRS_KEY, null)
            ?: allCardPairsDataSource.getAllCardPairs()
                .take(DEFAULT_UNLOCKED_CARD_PAIRS_COUNT)
                .map { it.id })
            .toLinkedSet()
            .also {
                sharedPreferences.edit { putStringSet(UNLOCKED_CARD_PAIRS_KEY, it) }
            }
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
