package com.wojdor.memolki.util.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.wojdor.memolki.domain.model.BoardModel
import javax.inject.Inject

class Analytics @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun logBoardStart(board: BoardModel) {
        firebaseAnalytics.logEvent(Event.BOARD_STARTED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${board.columns}x${board.rows}")
            putInt(Key.CARD_COUNT, board.columns * board.rows)
        })
    }

    fun logBoardComplete(
        board: BoardModel,
        mistakeCount: Int,
        level: Long,
        totalGamesCount: Long
    ) {
        firebaseAnalytics.logEvent(Event.BOARD_COMPLETED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${board.columns}x${board.rows}")
            putInt(Key.CARD_COUNT, board.columns * board.rows)
            putInt(Key.MISMATCH_COUNT, mistakeCount)
            putLong(Key.LEVEL, level)
            putLong(Key.TOTAL_GAMES_COUNT, totalGamesCount)
        })
    }

    fun logBoardAbandoned(board: BoardModel) {
        firebaseAnalytics.logEvent(Event.BOARD_ABANDONED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${board.columns}x${board.rows}")
        })
    }

    fun logAdRewardFromEndGame() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, Bundle().apply {
            putString(Key.PLACEMENT, Value.END_GAME)
        })
    }

    fun logAdRewardFromCollection() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, Bundle().apply {
            putString(Key.PLACEMENT, Value.COLLECTION)
        })
    }

    fun logAdRewardFromShop() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, Bundle().apply {
            putString(Key.PLACEMENT, Value.SHOP)
        })
    }

    fun logCardUnlockedWithCoins(totalUnlocked: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(Event.CARD_UNLOCKED, Bundle().apply {
            putString(Key.METHOD, Value.COINS)
            putInt(Key.TOTAL_UNLOCKED, totalUnlocked)
            putInt(Key.TOTAL_COUNT, totalCount)
            putInt(Key.COLLECTION_PCT, collectionPct(totalUnlocked, totalCount))
        })
    }

    fun logCardUnlockedWithAd(totalUnlocked: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(Event.CARD_UNLOCKED, Bundle().apply {
            putString(Key.METHOD, Value.AD)
            putInt(Key.TOTAL_UNLOCKED, totalUnlocked)
            putInt(Key.TOTAL_COUNT, totalCount)
            putInt(Key.COLLECTION_PCT, collectionPct(totalUnlocked, totalCount))
        })
    }

    private fun collectionPct(unlocked: Int, total: Int): Int =
        if (total <= 0) 0 else (unlocked * 100) / total

    fun logShopOpenedFromCollection() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, Bundle().apply {
            putString(Key.SOURCE, Value.COLLECTION)
        })
    }

    fun logShopOpenedFromInsufficientCoins() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, Bundle().apply {
            putString(Key.SOURCE, Value.INSUFFICIENT_COINS)
        })
    }

    fun logShopOpenedFromEndGame() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, Bundle().apply {
            putString(Key.SOURCE, Value.END_GAME)
        })
    }

    fun logShopOpenedFromDailyReward() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, Bundle().apply {
            putString(Key.SOURCE, Value.DAILY_REWARD)
        })
    }

    fun logCollectionOpenedFromLockedBoard() {
        firebaseAnalytics.logEvent(Event.COLLECTION_VIEWED, Bundle().apply {
            putString(Key.SOURCE, Value.LOCKED_BOARD)
        })
    }

    fun logPurchaseCompleted(product: String, priceMicros: Long, currencyCode: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, Bundle().apply {
            putString(Key.PRODUCT, product)
            putString(FirebaseAnalytics.Param.ITEM_ID, product)
            putDouble(FirebaseAnalytics.Param.VALUE, priceMicros / MICROS_PER_UNIT)
            putString(FirebaseAnalytics.Param.CURRENCY, currencyCode)
        })
    }

    fun logPurchaseFailed() {
        firebaseAnalytics.logEvent(Event.PURCHASE_FAILED, null)
    }

    fun logDailyStreakCollected(streakDay: Int, coinsEarned: Long) {
        firebaseAnalytics.logEvent(Event.DAILY_STREAK_COLLECTED, Bundle().apply {
            putInt(Key.STREAK_DAY, streakDay)
            putLong(Key.COINS_EARNED, coinsEarned)
        })
    }

    fun logCollectionViewed(unlockedCount: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(Event.COLLECTION_VIEWED, Bundle().apply {
            putInt(Key.UNLOCKED_COUNT, unlockedCount)
            putInt(Key.TOTAL_COUNT, totalCount)
        })
    }

    fun logShareClicked(isRewardAvailable: Boolean) {
        firebaseAnalytics.logEvent(Event.SHARE_CLICKED, Bundle().apply {
            putBoolean(Key.REWARD_AVAILABLE, isRewardAvailable)
        })
    }

    fun logNotificationEnabled(isEnabled: Boolean) {
        firebaseAnalytics.logEvent(Event.NOTIFICATION_ENABLED, Bundle().apply {
            putBoolean(Key.IS_ENABLED, isEnabled)
        })
    }

    fun logAppOpened(notificationType: String? = null, shortcutId: String? = null) {
        firebaseAnalytics.logEvent(Event.APP_OPENED, Bundle().apply {
            notificationType?.let { putString(Key.NOTIFICATION_TYPE, it) }
            shortcutId?.takeIf { it in ALLOWED_SHORTCUT_IDS }
                ?.let { putString(Key.SHORTCUT_ID, it) }
        })
    }

    fun logMoreAppsClicked() {
        firebaseAnalytics.logEvent(Event.MORE_APPS_CLICKED, null)
    }

    fun logCrossPromotionAppOpened(appId: String) {
        firebaseAnalytics.logEvent(Event.CROSS_PROMOTION_APP_CLICKED, Bundle().apply {
            putString(Key.APP_ID, appId)
            putString(Key.ACTION, Value.OPEN)
        })
    }

    fun logCrossPromotionStoreOpened(appId: String) {
        firebaseAnalytics.logEvent(Event.CROSS_PROMOTION_APP_CLICKED, Bundle().apply {
            putString(Key.APP_ID, appId)
            putString(Key.ACTION, Value.STORE)
        })
    }

    fun logAdShown(placement: String) {
        firebaseAnalytics.logEvent(Event.AD_SHOWN, Bundle().apply {
            putString(Key.PLACEMENT, placement)
        })
    }

    fun logAdDismissed(placement: String, wasRewardGranted: Boolean) {
        firebaseAnalytics.logEvent(Event.AD_DISMISSED, Bundle().apply {
            putString(Key.PLACEMENT, placement)
            putBoolean(Key.WAS_REWARD_GRANTED, wasRewardGranted)
        })
    }

    fun logInsufficientCoinsShown(coinsBalance: Long, cardCost: Int) {
        firebaseAnalytics.logEvent(Event.INSUFFICIENT_COINS_SHOWN, Bundle().apply {
            putLong(Key.COINS_BALANCE, coinsBalance)
            putInt(Key.CARD_COST, cardCost)
        })
    }

    fun logAppSessionStart(totalGamesPlayed: Long, unlockedCardsCount: Int) {
        firebaseAnalytics.logEvent(Event.APP_SESSION_START, Bundle().apply {
            putLong(Key.TOTAL_GAMES_PLAYED, totalGamesPlayed)
            putInt(Key.UNLOCKED_COUNT, unlockedCardsCount)
        })
    }

    fun logLanguageChanged(fromLanguage: String, toLanguage: String) {
        firebaseAnalytics.logEvent(Event.LANGUAGE_CHANGED, Bundle().apply {
            putString(Key.FROM_LANGUAGE, fromLanguage)
            putString(Key.TO_LANGUAGE, toLanguage)
        })
    }

    fun logCardPairDetailsViewed() {
        firebaseAnalytics.logEvent(Event.CARD_PAIR_DETAILS_VIEWED, null)
    }

    fun setUserLanguage(languageTag: String) {
        firebaseAnalytics.setUserProperty(Key.LANGUAGE, languageTag)
    }

    fun setNotificationPermission(isEnabled: Boolean) {
        firebaseAnalytics.setUserProperty(Key.NOTIFICATION_PERMISSION, isEnabled.toString())
    }

    fun logLeaderboardOpened() {
        firebaseAnalytics.logEvent(Event.LEADERBOARD_OPENED, null)
    }

    fun logDailyChallengeStart(epochDay: Long) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_STARTED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
        })
    }

    fun logDailyChallengeComplete(
        epochDay: Long,
        mistakeCount: Int,
        starCount: Int,
        timeMillis: Long,
        totalGamesCount: Long
    ) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_COMPLETED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
            putInt(Key.MISMATCH_COUNT, mistakeCount)
            putInt(Key.STAR_COUNT, starCount)
            putLong(Key.TIME_MILLIS, timeMillis)
            putLong(Key.TOTAL_GAMES_COUNT, totalGamesCount)
        })
    }

    fun logDailyChallengeShare(epochDay: Long, starCount: Int) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_SHARED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
            putInt(Key.STAR_COUNT, starCount)
        })
    }

    fun logDailyChallengeAlreadyPlayed(epochDay: Long) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_ALREADY_PLAYED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
        })
    }

    fun logDailyChallengeAbandoned(epochDay: Long) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_ABANDONED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
        })
    }

    fun logDailyChallengeHistoryOpened() {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_HISTORY_OPENED, null)
    }

    fun logDailyChallengeHistoryShareClicked(epochDay: Long) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_HISTORY_SHARED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
        })
    }

    fun logAdImpression(
        valueMicros: Long,
        currencyCode: String,
        adFormat: String,
        adUnitName: String
    ) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION, Bundle().apply {
            putString(FirebaseAnalytics.Param.AD_PLATFORM, Value.AD_PLATFORM_ADMOB)
            putString(FirebaseAnalytics.Param.AD_FORMAT, adFormat)
            putString(FirebaseAnalytics.Param.AD_UNIT_NAME, adUnitName)
            putString(FirebaseAnalytics.Param.CURRENCY, currencyCode)
            putDouble(FirebaseAnalytics.Param.VALUE, valueMicros / MICROS_PER_UNIT)
        })
    }

    companion object {
        private val ALLOWED_SHORTCUT_IDS = setOf("daily_reward", "play_game")
        private const val MICROS_PER_UNIT = 1_000_000.0
    }
}

private object Event {
    const val BOARD_STARTED = "board_started"
    const val BOARD_COMPLETED = "board_completed"
    const val BOARD_ABANDONED = "board_abandoned"
    const val AD_REWARD_EARNED = "ad_reward_earned"
    const val CARD_UNLOCKED = "card_unlocked"
    const val SHOP_OPENED = "shop_opened"
    const val PURCHASE_FAILED = "purchase_failed"
    const val DAILY_STREAK_COLLECTED = "daily_streak_collected"
    const val COLLECTION_VIEWED = "collection_viewed"
    const val SHARE_CLICKED = "share_clicked"
    const val NOTIFICATION_ENABLED = "notification_enabled"
    const val APP_OPENED = "app_opened"
    const val MORE_APPS_CLICKED = "more_apps_clicked"
    const val CROSS_PROMOTION_APP_CLICKED = "cross_promotion_app_clicked"
    const val AD_SHOWN = "ad_shown"
    const val AD_DISMISSED = "ad_dismissed"
    const val INSUFFICIENT_COINS_SHOWN = "insufficient_coins_shown"
    const val APP_SESSION_START = "app_session_start"
    const val LANGUAGE_CHANGED = "language_changed"
    const val CARD_PAIR_DETAILS_VIEWED = "card_pair_details_viewed"
    const val LEADERBOARD_OPENED = "leaderboard_opened"
    const val DAILY_CHALLENGE_STARTED = "daily_challenge_started"
    const val DAILY_CHALLENGE_COMPLETED = "daily_challenge_completed"
    const val DAILY_CHALLENGE_SHARED = "daily_challenge_shared"
    const val DAILY_CHALLENGE_ALREADY_PLAYED = "daily_challenge_already_played"
    const val DAILY_CHALLENGE_ABANDONED = "daily_challenge_abandoned"
    const val DAILY_CHALLENGE_HISTORY_OPENED = "daily_challenge_history_opened"
    const val DAILY_CHALLENGE_HISTORY_SHARED = "daily_challenge_history_shared"
}

private object Key {
    const val LEVEL_SIZE = "level_size"
    const val CARD_COUNT = "card_count"
    const val MISMATCH_COUNT = "mismatch_count"
    const val PLACEMENT = "placement"
    const val METHOD = "method"
    const val TOTAL_UNLOCKED = "total_unlocked"
    const val SOURCE = "source"
    const val PRODUCT = "product"
    const val STREAK_DAY = "streak_day"
    const val COINS_EARNED = "coins_earned"
    const val UNLOCKED_COUNT = "unlocked_count"
    const val TOTAL_COUNT = "total_count"
    const val REWARD_AVAILABLE = "reward_available"
    const val IS_ENABLED = "is_enabled"
    const val NOTIFICATION_TYPE = "notification_type"
    const val APP_ID = "app_id"
    const val ACTION = "action"
    const val WAS_REWARD_GRANTED = "was_reward_granted"
    const val COINS_BALANCE = "coins_balance"
    const val CARD_COST = "card_cost"
    const val TOTAL_GAMES_PLAYED = "total_games_played"
    const val FROM_LANGUAGE = "from_language"
    const val TO_LANGUAGE = "to_language"
    const val SHORTCUT_ID = "shortcut_id"
    const val LANGUAGE = "language"
    const val NOTIFICATION_PERMISSION = "notification_permission"
    const val CHALLENGE_NUMBER = "challenge_number"
    const val STAR_COUNT = "star_count"
    const val TIME_MILLIS = "time_millis"
    const val LEVEL = "level"
    const val TOTAL_GAMES_COUNT = "total_games_count"
    const val COLLECTION_PCT = "collection_pct"
}

private object Value {
    const val END_GAME = "end_game"
    const val DAILY_REWARD = "daily_reward"
    const val LOCKED_BOARD = "locked_board"
    const val COLLECTION = "collection"
    const val SHOP = "shop"
    const val COINS = "coins"
    const val AD = "ad"
    const val INSUFFICIENT_COINS = "insufficient_coins"
    const val OPEN = "open"
    const val STORE = "store"
    const val AD_PLATFORM_ADMOB = "admob"
}
