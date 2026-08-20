package com.wojdor.memolki.util.gameservices

import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.extension.topViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.GameKit.GKGameCenterControllerDelegateProtocol
import platform.GameKit.GKGameCenterViewController
import platform.GameKit.GKGameCenterViewControllerStateLeaderboards
import platform.GameKit.GKLeaderboard
import platform.GameKit.GKLocalPlayer
import platform.GameKit.authenticateHandler
import platform.GameKit.setGameCenterDelegate
import platform.GameKit.viewState
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalForeignApi::class)
class IosGameServices(
    private val mainDispatcher: CoroutineDispatcher
) : GameServices {

    // Retains the delegate: GKGameCenterViewController.gameCenterDelegate is a weak reference.
    private var gameCenterDelegate: GameCenterDelegate? = null

    override suspend fun signIn() {
        val localPlayer = GKLocalPlayer.local
        if (localPlayer.authenticated) return
        suspendCancellableCoroutine { continuation ->
            localPlayer.authenticateHandler = { viewController, _ ->
                if (viewController != null) {
                    topViewController()?.presentViewController(viewController, animated = true, completion = null)
                } else if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }
        }
    }

    override suspend fun isAuthenticated(): Boolean = GKLocalPlayer.local.authenticated

    override suspend fun submitTotalCoins(totalCoins: Long) {
        submitScore(LEADERBOARD_TOTAL_COINS, totalCoins)
    }

    override suspend fun submitTotalCardPairsMatched(totalCardPairsMatched: Long) {
        submitScore(LEADERBOARD_TOTAL_CARD_PAIRS_MATCHED, totalCardPairsMatched)
    }

    override suspend fun openLeaderboard() = withContext(mainDispatcher) {
        val presenter = topViewController() ?: return@withContext
        val delegate = GameCenterDelegate().also { gameCenterDelegate = it }
        val controller = GKGameCenterViewController()
        controller.viewState = GKGameCenterViewControllerStateLeaderboards
        controller.setGameCenterDelegate(delegate)
        presenter.presentViewController(controller, animated = true, completion = null)
    }

    @OptIn(ExperimentalNativeApi::class)
    private suspend fun submitScore(leaderboardId: String, score: Long) {
        if (Platform.isDebugBinary) {
            logE("Cannot submit score in debug build", IllegalStateException())
            return
        }
        if (!isAuthenticated()) return
        GKLeaderboard.submitScore(
            score = score,
            context = 0uL,
            player = GKLocalPlayer.local,
            leaderboardIDs = listOf(leaderboardId)
        ) { error ->
            if (error != null) {
                logE("Failed to submit score to $leaderboardId", Throwable(error.localizedDescription))
            }
        }
    }

    private companion object {
        // TODO(kmp-ios): must match the Game Center leaderboard IDs created in App Store Connect.
        const val LEADERBOARD_TOTAL_COINS = "total_coins"
        const val LEADERBOARD_TOTAL_CARD_PAIRS_MATCHED = "total_card_pairs_matched"
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class GameCenterDelegate : NSObject(), GKGameCenterControllerDelegateProtocol {
    override fun gameCenterViewControllerDidFinish(gameCenterViewController: GKGameCenterViewController) {
        gameCenterViewController.dismissViewControllerAnimated(true, completion = null)
    }
}
