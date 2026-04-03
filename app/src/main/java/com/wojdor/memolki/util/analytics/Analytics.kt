package com.wojdor.memolki.util.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.wojdor.memolki.domain.model.LevelModel
import javax.inject.Inject

class Analytics @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun logLevelStart(level: LevelModel) {
        firebaseAnalytics.logEvent(Event.LEVEL_STARTED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${level.columns}x${level.rows}")
            putInt(Key.CARD_COUNT, level.columns * level.rows)
        })
    }

    fun logLevelComplete(level: LevelModel, mistakeCount: Int) {
        firebaseAnalytics.logEvent(Event.LEVEL_COMPLETED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${level.columns}x${level.rows}")
            putInt(Key.CARD_COUNT, level.columns * level.rows)
            putInt(Key.MISMATCH_COUNT, mistakeCount)
        })
    }

    fun logLevelAbandoned(level: LevelModel) {
        firebaseAnalytics.logEvent(Event.LEVEL_ABANDONED, Bundle().apply {
            putString(Key.LEVEL_SIZE, "${level.columns}x${level.rows}")
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

    fun logCardUnlockedWithCoins(totalUnlocked: Int) {
        firebaseAnalytics.logEvent(Event.CARD_UNLOCKED, Bundle().apply {
            putString(Key.METHOD, Value.COINS)
            putInt(Key.TOTAL_UNLOCKED, totalUnlocked)
        })
    }

    fun logCardUnlockedWithAd(totalUnlocked: Int) {
        firebaseAnalytics.logEvent(Event.CARD_UNLOCKED, Bundle().apply {
            putString(Key.METHOD, Value.AD)
            putInt(Key.TOTAL_UNLOCKED, totalUnlocked)
        })
    }

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

    fun logPurchaseCompleted(product: String) {
        firebaseAnalytics.logEvent(Event.PURCHASE_COMPLETED, Bundle().apply {
            putString(Key.PRODUCT, product)
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
        timeMillis: Long
    ) {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_COMPLETED, Bundle().apply {
            putLong(Key.CHALLENGE_NUMBER, epochDay)
            putInt(Key.MISMATCH_COUNT, mistakeCount)
            putInt(Key.STAR_COUNT, starCount)
            putLong(Key.TIME_MILLIS, timeMillis)
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

    companion object {
        private val ALLOWED_SHORTCUT_IDS = setOf("daily_reward", "play_game")
    }
}

private object Event {
    const val LEVEL_STARTED = "level_started"
    const val LEVEL_COMPLETED = "level_completed"
    const val LEVEL_ABANDONED = "level_abandoned"
    const val AD_REWARD_EARNED = "ad_reward_earned"
    const val CARD_UNLOCKED = "card_unlocked"
    const val SHOP_OPENED = "shop_opened"
    const val PURCHASE_COMPLETED = "purchase_completed"
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
    const val CHALLENGE_NUMBER = "challenge_number"
    const val STAR_COUNT = "star_count"
    const val TIME_MILLIS = "time_millis"
}

private object Value {
    const val END_GAME = "end_game"
    const val COLLECTION = "collection"
    const val SHOP = "shop"
    const val COINS = "coins"
    const val AD = "ad"
    const val INSUFFICIENT_COINS = "insufficient_coins"
    const val OPEN = "open"
    const val STORE = "store"
}
