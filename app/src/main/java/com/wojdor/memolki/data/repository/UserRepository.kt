package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.user.UserLocalDataSource
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
        val currentCoins = getCoins().first()
        val newCoins = currentCoins + coins
        addTotalCoins(coins)
        val encryptedCoins = encryptor.encrypt(newCoins)
        userLocalDataSource.setEncryptedCoins(encryptedCoins)
    }

    fun getTotalCoins() = decryptLong(userLocalDataSource.encryptedTotalCoins)

    private suspend fun addTotalCoins(coins: Long) {
        val currentTotalCoins = getTotalCoins().first()
        val newTotalCoins = currentTotalCoins + coins
        val encryptedTotalCoins = encryptor.encrypt(newTotalCoins)
        userLocalDataSource.setEncryptedTotalCoins(encryptedTotalCoins)
    }

    fun getTotalMatchedCardPairCount() =
        decryptLong(userLocalDataSource.encryptedTotalMatchedCardPairCount)

    suspend fun incrementTotalMatchedCardPairCount() {
        var count = getTotalMatchedCardPairCount().first()
        count++
        val encryptedCount = encryptor.encrypt(count)
        userLocalDataSource.setEncryptedTotalMatchedCardPairCount(encryptedCount)
    }

    fun getTotalGamesPlayed() = decryptLong(userLocalDataSource.encryptedTotalGamesPlayed)

    suspend fun incrementTotalGamesPlayed() {
        var count = getTotalGamesPlayed().first()
        count++
        val encryptedCount = encryptor.encrypt(count)
        userLocalDataSource.setEncryptedTotalGamesPlayed(encryptedCount)
    }

    private fun decryptLong(flow: Flow<String?>): Flow<Long> {
        return flow.map { encryptedValue ->
            if (encryptedValue.isNullOrEmpty()) {
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
    }

    companion object {
        private const val DEFAULT_LONG_VALUE = 0L
    }
}
