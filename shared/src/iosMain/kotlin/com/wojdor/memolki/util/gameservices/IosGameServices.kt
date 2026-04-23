package com.wojdor.memolki.util.gameservices

// TODO(kmp-ios): replace with a GameKit-backed impl when iOS leaderboards ship.
class IosGameServices : GameServices {
    override suspend fun signIn() = Unit
    override suspend fun isAuthenticated(): Boolean = false
    override suspend fun submitTotalCoins(totalCoins: Long) = Unit
    override suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long) = Unit
    override suspend fun openLeaderboard() = Unit
}
