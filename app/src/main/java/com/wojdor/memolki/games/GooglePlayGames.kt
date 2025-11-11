package com.wojdor.memolki.games

import android.app.Activity
import android.content.Intent
import androidx.annotation.StringRes
import com.google.android.gms.games.PlayGames
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.R
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GooglePlayGames @Inject constructor() {

    suspend fun signIn(activity: Activity) {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn().await()
    }

    suspend fun isAuthenticated(activity: Activity): Boolean {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        return gamesSignInClient.isAuthenticated.await().isAuthenticated
    }

    suspend fun submitTotalCoins(
        activity: Activity,
        totalCoins: Long
    ) {
        submitScore(activity, R.string.leaderboard_total_coins_id, totalCoins)
    }

    suspend fun submitTotalCardPairsMatched(
        activity: Activity,
        totalCardPairsMatched: Long
    ) {
        submitScore(
            activity,
            R.string.leaderboard_total_card_pairs_matched_id,
            totalCardPairsMatched
        )
    }

    suspend fun getLeaderboardIntent(activity: Activity): Intent =
        PlayGames.getLeaderboardsClient(activity).allLeaderboardsIntent.await()

    private suspend fun submitScore(
        activity: Activity,
        @StringRes leaderboard: Int,
        score: Long
    ) {
        if (BuildConfig.DEBUG) {
            logE("Cannot submit score in debug build", IllegalStateException())
            return
        }
        if (isAuthenticated(activity)) {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.submitScore(
                activity.getString(leaderboard),
                score
            )
        }
    }
}
