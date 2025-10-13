package com.wojdor.memolki.data.local.card

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UnlockedCardPairsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val allCardPairsDataSource: AllCardPairsDataSource
) {

    suspend fun getUnlockedCardPairIds(): List<String> {
        val preferences = dataStore.data.first()
        return preferences[Key.UNLOCKED_CARD_PAIR_IDS]?.toList() ?: run {
            val defaultCardPairIds = allCardPairsDataSource.getAllCardPairs()
                .take(DEFAULT_UNLOCKED_CARD_PAIRS_COUNT)
                .map { it.id }
            dataStore.edit { it[Key.UNLOCKED_CARD_PAIR_IDS] = defaultCardPairIds.toSet() }
            defaultCardPairIds
        }
    }

    suspend fun addUnlockedCardPairId(unlockedCardPairId: String) {
        val currentUnlockedCardPairIds = getUnlockedCardPairIds().toSet()
        dataStore.edit {
            it[Key.UNLOCKED_CARD_PAIR_IDS] = currentUnlockedCardPairIds + unlockedCardPairId
        }
    }

    suspend fun areAllCardPairsUnlocked(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[Key.ARE_ALL_CARD_PAIRS_UNLOCKED] ?: false
    }

    suspend fun setAllCardPairsUnlocked() {
        dataStore.edit {
            it[Key.ARE_ALL_CARD_PAIRS_UNLOCKED] = true
        }
    }

    object Key {
        val UNLOCKED_CARD_PAIR_IDS = stringSetPreferencesKey("unlocked_card_pair_ids")
        val ARE_ALL_CARD_PAIRS_UNLOCKED = booleanPreferencesKey("are_all_card_pairs_unlocked")
    }

    companion object {
        private const val DEFAULT_UNLOCKED_CARD_PAIRS_COUNT = 5
    }
}
