package com.wojdor.memolki.util.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.wojdor.memolki.domain.model.BoardModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class AnalyticsTest {

    private val firebaseAnalytics: FirebaseAnalytics = mockk(relaxed = true)
    private val sut = Analytics(firebaseAnalytics)

    @Test
    fun `logBoardStart logs event with board size`() {
        // when
        sut.logBoardStart(BoardModel.Grid2x3(isUnlocked = true))

        // then
        verify { firebaseAnalytics.logEvent("board_started", any()) }
    }

    @Test
    fun `logBoardComplete logs event with board size and mistake count`() {
        // when
        sut.logBoardComplete(BoardModel.Grid2x3(isUnlocked = true), 5)

        // then
        verify { firebaseAnalytics.logEvent("board_completed", any()) }
    }

    @Test
    fun `logBoardAbandoned logs event with board size`() {
        // when
        sut.logBoardAbandoned(BoardModel.Grid2x3(isUnlocked = true))

        // then
        verify { firebaseAnalytics.logEvent("board_abandoned", any()) }
    }

    @Test
    fun `logAdRewardFromEndGame logs event`() {
        // when
        sut.logAdRewardFromEndGame()

        // then
        verify { firebaseAnalytics.logEvent("ad_reward_earned", any()) }
    }

    @Test
    fun `logAdRewardFromCollection logs event`() {
        // when
        sut.logAdRewardFromCollection()

        // then
        verify { firebaseAnalytics.logEvent("ad_reward_earned", any()) }
    }

    @Test
    fun `logAdRewardFromShop logs event`() {
        // when
        sut.logAdRewardFromShop()

        // then
        verify { firebaseAnalytics.logEvent("ad_reward_earned", any()) }
    }

    @Test
    fun `logCardUnlockedWithCoins logs event`() {
        // when
        sut.logCardUnlockedWithCoins(10)

        // then
        verify { firebaseAnalytics.logEvent("card_unlocked", any()) }
    }

    @Test
    fun `logCardUnlockedWithAd logs event`() {
        // when
        sut.logCardUnlockedWithAd(5)

        // then
        verify { firebaseAnalytics.logEvent("card_unlocked", any()) }
    }

    @Test
    fun `logShopOpenedFromCollection logs event`() {
        // when
        sut.logShopOpenedFromCollection()

        // then
        verify { firebaseAnalytics.logEvent("shop_opened", any()) }
    }

    @Test
    fun `logShopOpenedFromInsufficientCoins logs event`() {
        // when
        sut.logShopOpenedFromInsufficientCoins()

        // then
        verify { firebaseAnalytics.logEvent("shop_opened", any()) }
    }

    @Test
    fun `logShopOpenedFromEndGame logs event`() {
        // when
        sut.logShopOpenedFromEndGame()

        // then
        verify { firebaseAnalytics.logEvent("shop_opened", any()) }
    }

    @Test
    fun `logShopOpenedFromDailyReward logs event`() {
        // when
        sut.logShopOpenedFromDailyReward()

        // then
        verify { firebaseAnalytics.logEvent("shop_opened", any()) }
    }

    @Test
    fun `logCollectionOpenedFromLockedBoard logs event`() {
        // when
        sut.logCollectionOpenedFromLockedBoard()

        // then
        verify { firebaseAnalytics.logEvent("collection_viewed", any()) }
    }

    @Test
    fun `logPurchaseCompleted logs event with product`() {
        // when
        sut.logPurchaseCompleted("coins_small")

        // then
        verify { firebaseAnalytics.logEvent("purchase_completed", any()) }
    }

    @Test
    fun `logPurchaseFailed logs event`() {
        // when
        sut.logPurchaseFailed()

        // then
        verify { firebaseAnalytics.logEvent("purchase_failed", null) }
    }

    @Test
    fun `logDailyStreakCollected logs event`() {
        // when
        sut.logDailyStreakCollected(3, 50L)

        // then
        verify { firebaseAnalytics.logEvent("daily_streak_collected", any()) }
    }

    @Test
    fun `logCollectionViewed logs event`() {
        // when
        sut.logCollectionViewed(10, 50)

        // then
        verify { firebaseAnalytics.logEvent("collection_viewed", any()) }
    }

    @Test
    fun `logShareClicked logs event`() {
        // when
        sut.logShareClicked(true)

        // then
        verify { firebaseAnalytics.logEvent("share_clicked", any()) }
    }

    @Test
    fun `logNotificationEnabled logs event`() {
        // when
        sut.logNotificationEnabled(true)

        // then
        verify { firebaseAnalytics.logEvent("notification_enabled", any()) }
    }

    @Test
    fun `logAppOpened logs event`() {
        // when
        sut.logAppOpened("reminder", "daily_reward")

        // then
        verify { firebaseAnalytics.logEvent("app_opened", any()) }
    }

    @Test
    fun `logAppOpened with null params logs event`() {
        // when
        sut.logAppOpened()

        // then
        verify { firebaseAnalytics.logEvent("app_opened", any()) }
    }

    @Test
    fun `logAppOpened with disallowed shortcut ignores it`() {
        // when
        sut.logAppOpened(shortcutId = "unknown_shortcut")

        // then
        verify { firebaseAnalytics.logEvent("app_opened", any()) }
    }

    @Test
    fun `logMoreAppsClicked logs event`() {
        // when
        sut.logMoreAppsClicked()

        // then
        verify { firebaseAnalytics.logEvent("more_apps_clicked", null) }
    }

    @Test
    fun `logCrossPromotionAppOpened logs event`() {
        // when
        sut.logCrossPromotionAppOpened("com.example.app")

        // then
        verify { firebaseAnalytics.logEvent("cross_promotion_app_clicked", any()) }
    }

    @Test
    fun `logCrossPromotionStoreOpened logs event`() {
        // when
        sut.logCrossPromotionStoreOpened("com.example.app")

        // then
        verify { firebaseAnalytics.logEvent("cross_promotion_app_clicked", any()) }
    }

    @Test
    fun `logAdShown logs event with placement`() {
        // when
        sut.logAdShown("shop")

        // then
        verify { firebaseAnalytics.logEvent("ad_shown", any()) }
    }

    @Test
    fun `logAdDismissed logs event`() {
        // when
        sut.logAdDismissed("shop", true)

        // then
        verify { firebaseAnalytics.logEvent("ad_dismissed", any()) }
    }

    @Test
    fun `logInsufficientCoinsShown logs event`() {
        // when
        sut.logInsufficientCoinsShown(100L, 200)

        // then
        verify { firebaseAnalytics.logEvent("insufficient_coins_shown", any()) }
    }

    @Test
    fun `logAppSessionStart logs event`() {
        // when
        sut.logAppSessionStart(10L, 5)

        // then
        verify { firebaseAnalytics.logEvent("app_session_start", any()) }
    }

    @Test
    fun `logLanguageChanged logs event`() {
        // when
        sut.logLanguageChanged("en", "pl")

        // then
        verify { firebaseAnalytics.logEvent("language_changed", any()) }
    }

    @Test
    fun `logCardPairDetailsViewed logs event`() {
        // when
        sut.logCardPairDetailsViewed()

        // then
        verify { firebaseAnalytics.logEvent("card_pair_details_viewed", null) }
    }

    @Test
    fun `setUserLanguage sets user property`() {
        // when
        sut.setUserLanguage("en")

        // then
        verify { firebaseAnalytics.setUserProperty("language", "en") }
    }

    @Test
    fun `setNotificationPermission sets user property`() {
        // when
        sut.setNotificationPermission(true)

        // then
        verify { firebaseAnalytics.setUserProperty("notification_permission", "true") }
    }

    @Test
    fun `logLeaderboardOpened logs event`() {
        // when
        sut.logLeaderboardOpened()

        // then
        verify { firebaseAnalytics.logEvent("leaderboard_opened", null) }
    }

    @Test
    fun `logDailyChallengeStart logs event`() {
        // when
        sut.logDailyChallengeStart(100L)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_started", any()) }
    }

    @Test
    fun `logDailyChallengeComplete logs event`() {
        // when
        sut.logDailyChallengeComplete(100L, 2, 3, 5000L)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_completed", any()) }
    }

    @Test
    fun `logDailyChallengeShare logs event`() {
        // when
        sut.logDailyChallengeShare(100L, 3)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_shared", any()) }
    }

    @Test
    fun `logDailyChallengeAlreadyPlayed logs event`() {
        // when
        sut.logDailyChallengeAlreadyPlayed(100L)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_already_played", any()) }
    }

    @Test
    fun `logDailyChallengeAbandoned logs event`() {
        // when
        sut.logDailyChallengeAbandoned(100L)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_abandoned", any()) }
    }

    @Test
    fun `logDailyChallengeHistoryOpened logs event`() {
        // when
        sut.logDailyChallengeHistoryOpened()

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_history_opened", null) }
    }

    @Test
    fun `logDailyChallengeHistoryShareClicked logs event with epoch day`() {
        // when
        sut.logDailyChallengeHistoryShareClicked(20001L)

        // then
        verify { firebaseAnalytics.logEvent("daily_challenge_history_shared", any()) }
    }
}
