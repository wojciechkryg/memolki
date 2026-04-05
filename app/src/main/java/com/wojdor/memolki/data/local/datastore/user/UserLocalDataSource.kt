package com.wojdor.memolki.data.local.datastore.user

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
        transform: suspend (encryptedCoins: String?, encryptedTotalCoins: String?) -> Pair<String, String>
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

    suspend fun setEncryptedTotalCardPairsMatched(transform: suspend (encryptedValue: String?) -> String) {
        dataWrite.edit { prefs ->
            prefs[Key.TOTAL_MATCHED_CARD_PAIR_COUNT] =
                transform(prefs[Key.TOTAL_MATCHED_CARD_PAIR_COUNT])
        }
    }

    val encryptedTotalGamesPlayed: Flow<String?> =
        dataRead.map { it[Key.TOTAL_GAMES_PLAYED] }

    suspend fun setEncryptedTotalGamesPlayed(transform: suspend (encryptedValue: String?) -> String) {
        dataWrite.edit { prefs ->
            prefs[Key.TOTAL_GAMES_PLAYED] =
                transform(prefs[Key.TOTAL_GAMES_PLAYED])
        }
    }

    val encryptedUnlockedCardPairsFromAdsCount: Flow<String?> =
        dataRead.map { it[Key.UNLOCKED_CARD_PAIRS_FROM_ADS_COUNT] }

    suspend fun setEncryptedUnlockedCardPairsFromAdsCount(transform: suspend (encryptedValue: String?) -> String) {
        dataWrite.edit { prefs ->
            prefs[Key.UNLOCKED_CARD_PAIRS_FROM_ADS_COUNT] =
                transform(prefs[Key.UNLOCKED_CARD_PAIRS_FROM_ADS_COUNT])
        }
    }

    val encryptedLastShopAdShownTimestamp: Flow<String?> =
        dataRead.map { it[Key.LAST_SHOP_AD_SHOWN_TIMESTAMP] }

    suspend fun setEncryptedLastShopAdShownTimestamp(encryptedValue: String) {
        dataWrite.edit { prefs ->
            prefs[Key.LAST_SHOP_AD_SHOWN_TIMESTAMP] = encryptedValue
        }
    }

    val encryptedHasReceivedShareReward: Flow<String?> =
        dataRead.map { it[Key.HAS_RECEIVED_SHARE_REWARD] }

    suspend fun setEncryptedHasReceivedShareReward(encryptedValue: String) {
        dataWrite.edit { prefs ->
            prefs[Key.HAS_RECEIVED_SHARE_REWARD] = encryptedValue
        }
    }

    val encryptedDailyStreakCount: Flow<String?> =
        dataRead.map { it[Key.DAILY_STREAK_COUNT] }

    val encryptedLastDailyStreakCollectedTimestamp: Flow<String?> =
        dataRead.map { it[Key.LAST_DAILY_STREAK_COLLECTED_TIMESTAMP] }

    suspend fun setEncryptedDailyStreakData(
        transform: suspend (encryptedCount: String?, encryptedTimestamp: String?) -> Pair<String, String>
    ) {
        dataWrite.edit { prefs ->
            val (newCount, newTimestamp) = transform(
                prefs[Key.DAILY_STREAK_COUNT],
                prefs[Key.LAST_DAILY_STREAK_COLLECTED_TIMESTAMP]
            )
            prefs[Key.DAILY_STREAK_COUNT] = newCount
            prefs[Key.LAST_DAILY_STREAK_COLLECTED_TIMESTAMP] = newTimestamp
        }
    }

    fun encryptedLevelPlayedCount(levelId: String): Flow<String?> =
        dataRead.map { it[stringPreferencesKey("level_played_count_$levelId")] }

    suspend fun setEncryptedLevelPlayedCount(
        levelId: String,
        transform: suspend (encryptedValue: String?) -> String
    ) {
        val key = stringPreferencesKey("level_played_count_$levelId")
        dataWrite.edit { prefs -> prefs[key] = transform(prefs[key]) }
    }

    val fcmLanguageTopic: Flow<String?> = dataRead.map { it[Key.FCM_LANGUAGE_TOPIC] }

    suspend fun setFcmLanguageTopic(language: String) {
        dataWrite.edit { it[Key.FCM_LANGUAGE_TOPIC] = language }
    }

    private object Key {
        val COINS = stringPreferencesKey("coins")
        val TOTAL_COINS = stringPreferencesKey("total_coins")
        val TOTAL_MATCHED_CARD_PAIR_COUNT = stringPreferencesKey("total_matched_card_pair_count")
        val TOTAL_GAMES_PLAYED = stringPreferencesKey("total_games_played")
        val UNLOCKED_CARD_PAIRS_FROM_ADS_COUNT =
            stringPreferencesKey("unlocked_card_pairs_from_ads_count")
        val LAST_SHOP_AD_SHOWN_TIMESTAMP =
            stringPreferencesKey("last_shop_ad_shown_timestamp")
        val HAS_RECEIVED_SHARE_REWARD = stringPreferencesKey("has_received_share_reward")
        val DAILY_STREAK_COUNT = stringPreferencesKey("daily_streak_count")
        val LAST_DAILY_STREAK_COLLECTED_TIMESTAMP =
            stringPreferencesKey("last_daily_streak_collected_timestamp")
        val FCM_LANGUAGE_TOPIC = stringPreferencesKey("fcm_language_topic")
    }
}
