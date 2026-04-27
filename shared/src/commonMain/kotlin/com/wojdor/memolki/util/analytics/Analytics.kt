package com.wojdor.memolki.util.analytics

import com.wojdor.memolki.domain.model.BoardModel

interface Analytics {
    fun logBoardStart(board: BoardModel)
    fun logBoardComplete(board: BoardModel, mistakeCount: Int, level: Long, totalGamesCount: Long?)
    fun logBoardAbandoned(board: BoardModel)
    fun logAdRewardFromEndGame()
    fun logAdRewardFromCollection()
    fun logAdRewardFromShop()
    fun logCardUnlockedWithCoins(totalUnlocked: Int, totalCount: Int)
    fun logCardUnlockedWithAd(totalUnlocked: Int, totalCount: Int)
    fun logShopOpenedFromCollection()
    fun logShopOpenedFromInsufficientCoins()
    fun logShopOpenedFromEndGame()
    fun logShopOpenedFromDailyReward()
    fun logCollectionOpenedFromLockedBoard()
    fun logPurchaseCompleted(product: String, priceMicros: Long, currencyCode: String)
    fun logPurchaseFailed()
    fun logDailyStreakCollected(streakDay: Int, coinsEarned: Long)
    fun logCollectionViewed(unlockedCount: Int, totalCount: Int)
    fun logShareClicked(isRewardAvailable: Boolean)
    fun logNotificationEnabled(isEnabled: Boolean)
    fun logAppOpened(notificationType: String? = null, shortcutId: String? = null)
    fun logMoreAppsClicked()
    fun logCrossPromotionAppOpened(appId: String)
    fun logCrossPromotionStoreOpened(appId: String)
    fun logAdShown(placement: String)
    fun logAdDismissed(placement: String, wasRewardGranted: Boolean)
    fun logAdImpression(
        valueMicros: Long,
        currencyCode: String,
        adFormat: String,
        adUnitName: String,
        adSource: String?
    )
    fun logInsufficientCoinsShown(coinsBalance: Long, cardCost: Int)
    fun logAppSessionStart(totalGamesPlayed: Long, unlockedCardsCount: Int)
    fun logLanguageChanged(fromLanguage: String, toLanguage: String)
    fun logCardPairDetailsViewed()
    fun setUserLanguage(languageTag: String)
    fun setNotificationPermission(isEnabled: Boolean)
    fun logLeaderboardOpened()
    fun logDailyChallengeStart(epochDay: Long)
    fun logDailyChallengeComplete(
        epochDay: Long,
        mistakeCount: Int,
        starCount: Int,
        timeMillis: Long,
        totalGamesCount: Long?
    )
    fun logDailyChallengeShare(epochDay: Long, starCount: Int)
    fun logDailyChallengeAlreadyPlayed(epochDay: Long)
    fun logDailyChallengeAbandoned(epochDay: Long)
    fun logDailyChallengeHistoryOpened()
    fun logDailyChallengeHistoryShareClicked(epochDay: Long)
}
