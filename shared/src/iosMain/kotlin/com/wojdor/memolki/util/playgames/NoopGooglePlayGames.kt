package com.wojdor.memolki.util.playgames

// TODO(kmp-ios): replace with a Game Center-backed impl when iOS leaderboards ship.
class NoopGooglePlayGames : GooglePlayGames {
    override suspend fun signIn() = Unit
    override suspend fun isAuthenticated(): Boolean = false
    override suspend fun submitTotalCoins(totalCoins: Long) = Unit
    override suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long) = Unit
    override suspend fun openLeaderboard() = Unit
}
