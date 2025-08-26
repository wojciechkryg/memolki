package com.wojdor.memolki.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val encryptedCoins: Flow<String?> = dataStore.data.map { it[Key.COINS] }

    suspend fun setEncryptedCoins(encryptedCoins: String) {
        dataStore.edit { it[Key.COINS] = encryptedCoins }
    }

    val encryptedTotalCoins: Flow<String?> = dataStore.data.map { it[Key.TOTAL_COINS] }

    suspend fun setEncryptedTotalCoins(encryptedTotalCoins: String) {
        dataStore.edit { it[Key.TOTAL_COINS] = encryptedTotalCoins }
    }

    val encryptedTotalMatchedCardPairCount: Flow<String?> =
        dataStore.data.map { it[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] }

    suspend fun setEncryptedTotalMatchedCardPairCount(encryptedTotalMatchedCardPairCount: String) {
        dataStore.edit {
            it[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] = encryptedTotalMatchedCardPairCount
        }
    }

    val encryptedTotalGamesPlayed: Flow<String?> =
        dataStore.data.map { it[Key.TOTAL_GAMES_PLAYED] }

    suspend fun setEncryptedTotalGamesPlayed(totalGamesPlayed: String) {
        dataStore.edit { it[Key.TOTAL_GAMES_PLAYED] = totalGamesPlayed }
    }

    private object Key {
        val COINS = stringPreferencesKey("coins")
        val TOTAL_COINS = stringPreferencesKey("total_coins")
        val TOTAL_MATCHED_CARD_PAIR_COUNT = stringPreferencesKey("total_matched_card_pair_count")
        val TOTAL_GAMES_PLAYED = stringPreferencesKey("total_games_played")
    }
}
