package com.wojdor.memolki.util.gameservices

interface GameServices {
    suspend fun signIn()
    suspend fun isAuthenticated(): Boolean
    suspend fun submitTotalCoins(totalCoins: Long)
    suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long)
    suspend fun openLeaderboard()
}
