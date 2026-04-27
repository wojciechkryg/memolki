package com.wojdor.memolki.test.fake

import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.util.analytics.Analytics

class FakeAnalytics : Analytics {
    val events = mutableListOf<String>()

    var lastBoardStart: BoardModel? = null
        private set
    var lastBoardComplete: Triple<BoardModel, Int, Long>? = null
        private set
    var lastBoardCompleteTotalGames: Long? = null
        private set
    var lastBoardAbandoned: BoardModel? = null
        private set
    var adRewardFromEndGameCount: Int = 0
        private set
    var adRewardFromCollectionCount: Int = 0
        private set
    var adRewardFromShopCount: Int = 0
        private set
    var lastCardUnlockedWithCoins: Pair<Int, Int>? = null
        private set
    var lastCardUnlockedWithAd: Pair<Int, Int>? = null
        private set
    var shopOpenedFromCollectionCount: Int = 0
        private set
    var shopOpenedFromInsufficientCoinsCount: Int = 0
        private set
    var shopOpenedFromEndGameCount: Int = 0
        private set
    var shopOpenedFromDailyRewardCount: Int = 0
        private set
    var collectionOpenedFromLockedBoardCount: Int = 0
        private set
    var lastPurchaseCompleted: Triple<String, Long, String>? = null
        private set
    var purchaseFailedCount: Int = 0
        private set
    var lastDailyStreakCollected: Pair<Int, Long>? = null
        private set
    var lastCollectionViewed: Pair<Int, Int>? = null
        private set
    var lastShareClicked: Boolean? = null
        private set
    var lastNotificationEnabled: Boolean? = null
        private set
    var lastAppOpened: Pair<String?, String?>? = null
        private set
    var moreAppsClickedCount: Int = 0
        private set
    var lastCrossPromotionAppOpened: String? = null
        private set
    var lastCrossPromotionStoreOpened: String? = null
        private set
    var lastAdShown: String? = null
        private set
    var lastAdDismissed: Pair<String, Boolean>? = null
        private set
    var lastAdImpression: AdImpression? = null
        private set
    var lastInsufficientCoinsShown: Pair<Long, Int>? = null
        private set
    var lastAppSessionStart: Pair<Long, Int>? = null
        private set
    var lastLanguageChanged: Pair<String, String>? = null
        private set
    var cardPairDetailsViewedCount: Int = 0
        private set
    var lastUserLanguage: String? = null
        private set
    var lastNotificationPermission: Boolean? = null
        private set
    var leaderboardOpenedCount: Int = 0
        private set
    var lastDailyChallengeStart: Long? = null
        private set
    var lastDailyChallengeComplete: DailyChallengeComplete? = null
        private set
    var lastDailyChallengeShare: Pair<Long, Int>? = null
        private set
    var lastDailyChallengeAlreadyPlayed: Long? = null
        private set
    var lastDailyChallengeAbandoned: Long? = null
        private set
    var dailyChallengeHistoryOpenedCount: Int = 0
        private set
    var lastDailyChallengeHistoryShareClicked: Long? = null
        private set

    data class AdImpression(
        val valueMicros: Long,
        val currencyCode: String,
        val adFormat: String,
        val adUnitName: String,
        val adSource: String?
    )

    data class DailyChallengeComplete(
        val epochDay: Long,
        val mistakeCount: Int,
        val starCount: Int,
        val timeMillis: Long,
        val totalGamesCount: Long?
    )

    override fun logBoardStart(board: BoardModel) {
        events += "board_started"
        lastBoardStart = board
    }

    override fun logBoardComplete(
        board: BoardModel,
        mistakeCount: Int,
        level: Long,
        totalGamesCount: Long?
    ) {
        events += "board_completed"
        lastBoardComplete = Triple(board, mistakeCount, level)
        lastBoardCompleteTotalGames = totalGamesCount
    }

    override fun logBoardAbandoned(board: BoardModel) {
        events += "board_abandoned"
        lastBoardAbandoned = board
    }

    override fun logAdRewardFromEndGame() {
        events += "ad_reward_earned"
        adRewardFromEndGameCount++
    }

    override fun logAdRewardFromCollection() {
        events += "ad_reward_earned"
        adRewardFromCollectionCount++
    }

    override fun logAdRewardFromShop() {
        events += "ad_reward_earned"
        adRewardFromShopCount++
    }

    override fun logCardUnlockedWithCoins(totalUnlocked: Int, totalCount: Int) {
        events += "card_unlocked"
        lastCardUnlockedWithCoins = totalUnlocked to totalCount
    }

    override fun logCardUnlockedWithAd(totalUnlocked: Int, totalCount: Int) {
        events += "card_unlocked"
        lastCardUnlockedWithAd = totalUnlocked to totalCount
    }

    override fun logShopOpenedFromCollection() {
        events += "shop_opened"
        shopOpenedFromCollectionCount++
    }

    override fun logShopOpenedFromInsufficientCoins() {
        events += "shop_opened"
        shopOpenedFromInsufficientCoinsCount++
    }

    override fun logShopOpenedFromEndGame() {
        events += "shop_opened"
        shopOpenedFromEndGameCount++
    }

    override fun logShopOpenedFromDailyReward() {
        events += "shop_opened"
        shopOpenedFromDailyRewardCount++
    }

    override fun logCollectionOpenedFromLockedBoard() {
        events += "collection_viewed"
        collectionOpenedFromLockedBoardCount++
    }

    override fun logPurchaseCompleted(product: String, priceMicros: Long, currencyCode: String) {
        events += "purchase"
        lastPurchaseCompleted = Triple(product, priceMicros, currencyCode)
    }

    override fun logPurchaseFailed() {
        events += "purchase_failed"
        purchaseFailedCount++
    }

    override fun logDailyStreakCollected(streakDay: Int, coinsEarned: Long) {
        events += "daily_streak_collected"
        lastDailyStreakCollected = streakDay to coinsEarned
    }

    override fun logCollectionViewed(unlockedCount: Int, totalCount: Int) {
        events += "collection_viewed"
        lastCollectionViewed = unlockedCount to totalCount
    }

    override fun logShareClicked(isRewardAvailable: Boolean) {
        events += "share_clicked"
        lastShareClicked = isRewardAvailable
    }

    override fun logNotificationEnabled(isEnabled: Boolean) {
        events += "notification_enabled"
        lastNotificationEnabled = isEnabled
    }

    override fun logAppOpened(notificationType: String?, shortcutId: String?) {
        events += "app_opened"
        lastAppOpened = notificationType to shortcutId
    }

    override fun logMoreAppsClicked() {
        events += "more_apps_clicked"
        moreAppsClickedCount++
    }

    override fun logCrossPromotionAppOpened(appId: String) {
        events += "cross_promotion_app_clicked"
        lastCrossPromotionAppOpened = appId
    }

    override fun logCrossPromotionStoreOpened(appId: String) {
        events += "cross_promotion_app_clicked"
        lastCrossPromotionStoreOpened = appId
    }

    override fun logAdShown(placement: String) {
        events += "ad_shown"
        lastAdShown = placement
    }

    override fun logAdDismissed(placement: String, wasRewardGranted: Boolean) {
        events += "ad_dismissed"
        lastAdDismissed = placement to wasRewardGranted
    }

    override fun logAdImpression(
        valueMicros: Long,
        currencyCode: String,
        adFormat: String,
        adUnitName: String,
        adSource: String?
    ) {
        events += "ad_impression"
        lastAdImpression = AdImpression(valueMicros, currencyCode, adFormat, adUnitName, adSource)
    }

    override fun logInsufficientCoinsShown(coinsBalance: Long, cardCost: Int) {
        events += "insufficient_coins_shown"
        lastInsufficientCoinsShown = coinsBalance to cardCost
    }

    override fun logAppSessionStart(totalGamesPlayed: Long, unlockedCardsCount: Int) {
        events += "app_session_start"
        lastAppSessionStart = totalGamesPlayed to unlockedCardsCount
    }

    override fun logLanguageChanged(fromLanguage: String, toLanguage: String) {
        events += "language_changed"
        lastLanguageChanged = fromLanguage to toLanguage
    }

    override fun logCardPairDetailsViewed() {
        events += "card_pair_details_viewed"
        cardPairDetailsViewedCount++
    }

    override fun setUserLanguage(languageTag: String) {
        lastUserLanguage = languageTag
    }

    override fun setNotificationPermission(isEnabled: Boolean) {
        lastNotificationPermission = isEnabled
    }

    override fun logLeaderboardOpened() {
        events += "leaderboard_opened"
        leaderboardOpenedCount++
    }

    override fun logDailyChallengeStart(epochDay: Long) {
        events += "daily_challenge_started"
        lastDailyChallengeStart = epochDay
    }

    override fun logDailyChallengeComplete(
        epochDay: Long,
        mistakeCount: Int,
        starCount: Int,
        timeMillis: Long,
        totalGamesCount: Long?
    ) {
        events += "daily_challenge_completed"
        lastDailyChallengeComplete = DailyChallengeComplete(
            epochDay, mistakeCount, starCount, timeMillis, totalGamesCount
        )
    }

    override fun logDailyChallengeShare(epochDay: Long, starCount: Int) {
        events += "daily_challenge_shared"
        lastDailyChallengeShare = epochDay to starCount
    }

    override fun logDailyChallengeAlreadyPlayed(epochDay: Long) {
        events += "daily_challenge_already_played"
        lastDailyChallengeAlreadyPlayed = epochDay
    }

    override fun logDailyChallengeAbandoned(epochDay: Long) {
        events += "daily_challenge_abandoned"
        lastDailyChallengeAbandoned = epochDay
    }

    override fun logDailyChallengeHistoryOpened() {
        events += "daily_challenge_history_opened"
        dailyChallengeHistoryOpenedCount++
    }

    override fun logDailyChallengeHistoryShareClicked(epochDay: Long) {
        events += "daily_challenge_history_shared"
        lastDailyChallengeHistoryShareClicked = epochDay
    }
}
