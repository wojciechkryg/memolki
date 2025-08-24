package com.wojdor.memolki.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val coins: Flow<Long> = dataStore.data.map { it[Key.COINS] ?: DEFAULT_COUNT }

    suspend fun setCoins(coins: Long) {
        dataStore.edit { it[Key.COINS] = coins }
    }

    val totalCoins: Flow<Long> =
        dataStore.data.map { it[Key.TOTAL_COINS] ?: DEFAULT_COUNT }

    suspend fun setTotalCoins(totalCoins: Long) {
        dataStore.edit { it[Key.TOTAL_COINS] = totalCoins }
    }

    val totalMatchedCardPairCount: Flow<Long> =
        dataStore.data.map { it[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] ?: DEFAULT_COUNT }

    suspend fun setTotalMatchedCardPairCount(totalMatchedCardPairCount: Long) {
        dataStore.edit { it[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] = totalMatchedCardPairCount }
    }

    val totalGamesPlayed: Flow<Long> =
        dataStore.data.map { it[Key.TOTAL_GAMES_PLAYED] ?: DEFAULT_COUNT }

    suspend fun setTotalGamesPlayed(totalGamesPlayed: Long) {
        dataStore.edit { it[Key.TOTAL_GAMES_PLAYED] = totalGamesPlayed }
    }

    private object Key {
        val COINS = longPreferencesKey("coins")
        val TOTAL_COINS = longPreferencesKey("total_coins")
        val TOTAL_MATCHED_CARD_PAIR_COUNT = longPreferencesKey("total_matched_card_pair_count")
        val TOTAL_GAMES_PLAYED = longPreferencesKey("total_games_played")
    }

    companion object {
        private const val DEFAULT_COUNT = 0L
    }
}
