package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.datastore.user.UserLocalDataSource
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val encryptor: Encryptor,
    private val userLocalDataSource: UserLocalDataSource
) {

    fun getCoins() = decryptLong(userLocalDataSource.encryptedCoins)

    suspend fun addCoins(coins: Long) {
        require(coins >= 0) { "coins must be >= 0" }
        userLocalDataSource.setEncryptedCoinsAndTotalCoins { encryptedCoins, encryptedTotalCoins ->
            val currentCoins = decryptLong(encryptedCoins)
            val currentTotalCoins = decryptLong(encryptedTotalCoins)
            val newCoins = currentCoins + coins
            val newTotalCoins = currentTotalCoins + coins
            encryptor.encrypt(newCoins) to encryptor.encrypt(newTotalCoins)
        }
    }

    suspend fun removeCoins(coins: Long) {
        require(coins >= 0) { "coins must be >= 0" }
        userLocalDataSource.setEncryptedCoinsAndTotalCoins { encryptedCoins, encryptedTotalCoins ->
            val currentCoins = decryptLong(encryptedCoins)
            val currentTotalCoins = decryptLong(encryptedTotalCoins)
            val newCoins = currentCoins - coins
            encryptor.encrypt(newCoins) to encryptor.encrypt(currentTotalCoins)
        }
    }

    fun getTotalCoins() = decryptLong(userLocalDataSource.encryptedTotalCoins)

    fun getTotalCardPairsMatched() =
        decryptLong(userLocalDataSource.encryptedTotalCardPairsMatched)

    suspend fun incrementTotalCardPairsMatched(): Long {
        userLocalDataSource.setEncryptedTotalCardPairsMatched { encryptedCount ->
            val totalCardPairsMatched = decryptLong(encryptedCount)
            encryptor.encrypt(totalCardPairsMatched + 1)
        }
        return getTotalCardPairsMatched().first()
    }

    fun getTotalGamesPlayed() = decryptLong(userLocalDataSource.encryptedTotalGamesPlayed)

    suspend fun incrementTotalGamesPlayed() {
        userLocalDataSource.setEncryptedTotalGamesPlayed { encryptedCount ->
            val count = decryptLong(encryptedCount)
            encryptor.encrypt(count + 1)
        }
    }

    fun getUnlockedCardPairsFromAdsCount() =
        decryptLong(userLocalDataSource.encryptedUnlockedCardPairsFromAdsCount)

    suspend fun incrementUnlockedCardPairsFromAdsCount() {
        userLocalDataSource.setEncryptedUnlockedCardPairsFromAdsCount { encryptedCount ->
            val count = decryptLong(encryptedCount)
            encryptor.encrypt(count + 1)
        }
    }

    fun getLevel(boardId: String): Flow<Long> =
        decryptLong(userLocalDataSource.encryptedLevel(boardId))
            .map { it.coerceAtLeast(DEFAULT_LEVEL) }

    suspend fun incrementLevel(boardId: String): Long {
        userLocalDataSource.setEncryptedLevel(boardId) { encrypted ->
            val count = decryptLong(encrypted)
                .coerceAtLeast(DEFAULT_LEVEL)
            encryptor.encrypt(count + 1)
        }
        return getLevel(boardId).first()
    }

    fun getHasReceivedShareReward(): Flow<Boolean> =
        decryptLong(userLocalDataSource.encryptedHasReceivedShareReward).map { it == 1L }

    suspend fun setHasReceivedShareReward() {
        userLocalDataSource.setEncryptedHasReceivedShareReward(encryptor.encrypt(1L))
    }

    fun getDailyStreakCount() = decryptLong(userLocalDataSource.encryptedDailyStreakCount)

    fun getLastDailyStreakCollectedTimestamp() =
        decryptLong(userLocalDataSource.encryptedLastDailyStreakCollectedTimestamp)

    suspend fun setDailyStreakData(count: Long, timestamp: Long) {
        require(count >= 0) { "count must be >= 0" }
        require(timestamp >= 0) { "timestamp must be >= 0" }
        userLocalDataSource.setEncryptedDailyStreakData { _, _ ->
            encryptor.encrypt(count) to encryptor.encrypt(timestamp)
        }
    }

    fun getLastShopAdShownTimestamp() =
        decryptLong(userLocalDataSource.encryptedLastShopAdShownTimestamp)

    suspend fun setLastShopAdShownTimestamp(timestamp: Long) {
        userLocalDataSource.setEncryptedLastShopAdShownTimestamp(encryptor.encrypt(timestamp))
    }

    private suspend fun decryptLong(encryptedValue: String?): Long {
        return if (encryptedValue.isNullOrEmpty()) {
            DEFAULT_LONG_VALUE
        } else {
            try {
                encryptor.decrypt(encryptedValue)
            } catch (error: Exception) {
                logE("Decryption error", error)
                DEFAULT_LONG_VALUE
            }
        }
    }

    private fun decryptLong(flow: Flow<String?>): Flow<Long> {
        return flow.map { decryptLong(it) }
    }

    companion object {
        private const val DEFAULT_LONG_VALUE = 0L
        private const val DEFAULT_LEVEL = 1L
    }
}
