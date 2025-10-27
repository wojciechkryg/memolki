package com.wojdor.memolki.games

import android.app.Activity
import com.google.android.gms.games.PlayGames
import com.wojdor.memolki.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayGamesRepository @Inject constructor() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    suspend fun signIn(activity: Activity) {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        val result = gamesSignInClient.signIn().await()
        _isAuthenticated.value = result.isAuthenticated
    }

    suspend fun submitScore(activity: Activity, totalCoins: Long, totalCardPairsMatched: Long) {
        if (_isAuthenticated.value) {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.submitScore(
                activity.getString(R.string.leaderboard_total_coins_id),
                totalCoins
            )
            leaderboardsClient.submitScore(
                activity.getString(R.string.leaderboard_total_card_pairs_matched_id),
                totalCardPairsMatched
            )
        }
    }

    suspend fun getLeaderboardIntent(activity: Activity) =
        PlayGames.getLeaderboardsClient(activity).allLeaderboardsIntent.await()
}
