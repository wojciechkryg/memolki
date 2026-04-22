package com.wojdor.memolki.util.playgames

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.gms.games.PlayGames
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.R
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.tasks.await
import java.lang.ref.WeakReference

open class AndroidGooglePlayGames(context: Context) : GooglePlayGames {

    private var currentActivityRef: WeakReference<Activity>? = null

    init {
        (context.applicationContext as? Application)?.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityResumed(activity: Activity) {
                    currentActivityRef = WeakReference(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    if (currentActivityRef?.get() === activity) currentActivityRef = null
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
                override fun onActivityDestroyed(activity: Activity) = Unit
            }
        )
    }

    private fun requireActivity(): Activity? = currentActivityRef?.get()

    override suspend fun signIn() {
        val activity = requireActivity() ?: return
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn().await()
    }

    override suspend fun isAuthenticated(): Boolean {
        val activity = requireActivity() ?: return false
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
        val activity = requireActivity() ?: return
        val intent = PlayGames.getLeaderboardsClient(activity).allLeaderboardsIntent.await()
        activity.startActivity(intent)
    }

    private suspend fun submitScore(@StringRes leaderboard: Int, score: Long) {
        if (BuildConfig.DEBUG) {
            logE("Cannot submit score in debug build", IllegalStateException())
            return
        }
        val activity = requireActivity() ?: return
        if (isAuthenticated()) {
            val leaderboardsClient = PlayGames.getLeaderboardsClient(activity)
            leaderboardsClient.submitScore(
                activity.getString(leaderboard),
                score
            )
        }
    }
}
