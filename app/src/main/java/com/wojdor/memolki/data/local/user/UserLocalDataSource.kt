package com.wojdor.memolki.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataWrite: DataStore<Preferences>
) {
    private val dataRead: Flow<Preferences> = dataWrite.data.catch { _ ->
        emit(emptyPreferences())
    }

    val encryptedCoins: Flow<String?> = dataRead.map { it[Key.COINS] }

    val encryptedTotalCoins: Flow<String?> = dataRead.map { it[Key.TOTAL_COINS] }

    suspend fun setEncryptedCoinsAndTotalCoins(
        transform: (encryptedCoins: String?, encryptedTotalCoins: String?) -> Pair<String, String>
    ) {
        dataWrite.edit {
            val currentEncryptedCoins = it[Key.COINS]
            val currentEncryptedTotalCoins = it[Key.TOTAL_COINS]
            val (newEncryptedCoins, newEncryptedTotalCoins) = transform(
                currentEncryptedCoins,
                currentEncryptedTotalCoins
            )
            it[Key.COINS] = newEncryptedCoins
            it[Key.TOTAL_COINS] = newEncryptedTotalCoins
        }
    }

    val encryptedTotalCardPairsMatched: Flow<String?> =
        dataRead.map { it[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] }

    suspend fun setEncryptedTotalCardPairsMatched(
        transform: (encryptedValue: String?) -> String
    ) {
        dataWrite.edit { prefs ->
            prefs[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] =
                transform(prefs[Key.TOTAL_MATCHED_CARD_PAIR_COUNT])
        }
    }

    val encryptedTotalGamesPlayed: Flow<String?> =
        dataRead.map { it[Key.TOTAL_GAMES_PLAYED] }

    suspend fun setEncryptedTotalGamesPlayed(
        transform: (encryptedValue: String?) -> String
    ) {
        dataWrite.edit { prefs ->
            prefs[Key.TOTAL_GAMES_PLAYED] =
                transform(prefs[Key.TOTAL_GAMES_PLAYED])
        }
    }

    private object Key {
        val COINS = stringPreferencesKey("coins")
        val TOTAL_COINS = stringPreferencesKey("total_coins")
        val TOTAL_MATCHED_CARD_PAIR_COUNT = stringPreferencesKey("total_matched_card_pair_count")
        val TOTAL_GAMES_PLAYED = stringPreferencesKey("total_games_played")
    }
}
