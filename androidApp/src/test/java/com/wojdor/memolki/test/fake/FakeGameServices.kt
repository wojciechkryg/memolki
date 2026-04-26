package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.gameservices.GameServices

class FakeGameServices : GameServices {
    var isAuthenticated: Boolean = false

    var signInCount: Int = 0
        private set
    var lastSubmittedTotalCoins: Long? = null
        private set
    var lastSubmittedTotalCardPairsMatched: Long? = null
        private set
    var openLeaderboardCount: Int = 0
        private set

    override suspend fun signIn() {
        signInCount++
        isAuthenticated = true
    }

    override suspend fun isAuthenticated(): Boolean = isAuthenticated

    override suspend fun submitTotalCoins(totalCoins: Long) {
        lastSubmittedTotalCoins = totalCoins
    }

    override suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long) {
        lastSubmittedTotalCardPairsMatched = totalCardPairsMatched
    }

    override suspend fun openLeaderboard() {
        openLeaderboardCount++
    }
}
