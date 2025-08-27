package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val encryptor: Encryptor,
    private val userLocalDataSource: UserLocalDataSource
) {

    fun getCoins() = decryptLong(userLocalDataSource.encryptedCoins)

    suspend fun addCoins(coins: Long) {
        userLocalDataSource.setEncryptedCoinsAndTotalCoins { encryptedCoins, encryptedTotalCoins ->
            val currentCoins = decryptLong(encryptedCoins)
            val currentTotalCoins = decryptLong(encryptedTotalCoins)
            val newCoins = currentCoins + coins
            val newTotalCoins = currentTotalCoins + coins
            encryptor.encrypt(newCoins) to encryptor.encrypt(newTotalCoins)
        }
    }

    fun getTotalCoins() = decryptLong(userLocalDataSource.encryptedTotalCoins)

    fun getTotalCardPairsMatched() =
        decryptLong(userLocalDataSource.encryptedTotalCardPairsMatched)

    suspend fun incrementTotalCardPairsMatched() {
        userLocalDataSource.setEncryptedTotalCardPairsMatched { encryptedCount ->
            val totalCardPairsMatched = decryptLong(encryptedCount)
            encryptor.encrypt(totalCardPairsMatched + 1)
        }
    }

    fun getTotalGamesPlayed() = decryptLong(userLocalDataSource.encryptedTotalGamesPlayed)

    suspend fun incrementTotalGamesPlayed() {
        userLocalDataSource.setEncryptedTotalGamesPlayed { encryptedCount ->
            val count = decryptLong(encryptedCount)
            encryptor.encrypt(count + 1)
        }
    }

    private fun decryptLong(encryptedValue: String?): Long {
        return if (encryptedValue.isNullOrEmpty()) {
            DEFAULT_LONG_VALUE
        } else {
            try {
                encryptor.decrypt(encryptedValue)
            } catch (error: Exception) {
                logE("Decryption error", error)
                throw error
            }
        }
    }

    private fun decryptLong(flow: Flow<String?>): Flow<Long> {
        return flow.map { decryptLong(it) }
    }

    companion object {
        private const val DEFAULT_LONG_VALUE = 0L
    }
}
