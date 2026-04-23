package com.wojdor.memolki.util.gameservices

import androidx.annotation.StringRes
import com.google.android.gms.games.PlayGames
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.R
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.provider.ActivityProvider
import kotlinx.coroutines.tasks.await

open class AndroidGameServices(private val activityProvider: ActivityProvider) : GameServices {

    override suspend fun signIn() {
        val activity = activityProvider.current ?: return
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn().await()
    }

    override suspend fun isAuthenticated(): Boolean {
        val activity = activityProvider.current ?: return false
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        return gamesSignInClient.isAuthenticated.await().isAuthenticated
    }

    override suspend fun submitTotalCoins(totalCoins: Long) {
        submitScore(R.string.leaderboard_total_coins_id, totalCoins)
    }

    override suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long) {
        submitScore(R.string.leaderboard_total_card_pairs_matched_id, totalCardPairsMatched)
    }

    override suspend fun openLeaderboard() {
        val activity = activityProvider.current ?: return
        val intent = PlayGames.getLeaderboardsClient(activity).allLeaderboardsIntent.await()
        activity.startActivity(intent)
    }

    private suspend fun submitScore(@StringRes leaderboard: Int, score: Long) {
        if (BuildConfig.DEBUG) {
            logE("Cannot submit score in debug build", IllegalStateException())
            return
        }
        val activity = activityProvider.current ?: return
        if (isAuthenticated()) {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.submitScore(
                activity.getString(leaderboard),
                score
            )
        }
    }
}
