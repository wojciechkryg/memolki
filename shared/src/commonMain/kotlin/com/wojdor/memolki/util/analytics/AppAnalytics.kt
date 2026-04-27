package com.wojdor.memolki.util.analytics

import com.wojdor.memolki.domain.model.BoardModel
import dev.gitlive.firebase.analytics.FirebaseAnalytics

class AppAnalytics(
    private val firebaseAnalytics: FirebaseAnalytics
) : Analytics {

    override fun logBoardStart(board: BoardModel) {
        firebaseAnalytics.logEvent(
            Event.BOARD_STARTED,
            mapOf(
                Key.LEVEL_SIZE to "${board.columns}x${board.rows}",
                Key.CARD_COUNT to board.columns * board.rows
            )
        )
    }

    override fun logBoardComplete(
        board: BoardModel,
        mistakeCount: Int,
        level: Long,
        totalGamesCount: Long?
    ) {
        firebaseAnalytics.logEvent(
            Event.BOARD_COMPLETED,
            buildMap {
                put(Key.LEVEL_SIZE, "${board.columns}x${board.rows}")
                put(Key.CARD_COUNT, board.columns * board.rows)
                put(Key.MISMATCH_COUNT, mistakeCount)
                put(Key.LEVEL, level)
                totalGamesCount?.let { put(Key.TOTAL_GAMES_COUNT, it) }
            }
        )
    }

    override fun logBoardAbandoned(board: BoardModel) {
        firebaseAnalytics.logEvent(
            Event.BOARD_ABANDONED,
            mapOf(Key.LEVEL_SIZE to "${board.columns}x${board.rows}")
        )
    }

    override fun logAdRewardFromEndGame() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, mapOf(Key.PLACEMENT to Value.END_GAME))
    }

    override fun logAdRewardFromCollection() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, mapOf(Key.PLACEMENT to Value.COLLECTION))
    }

    override fun logAdRewardFromShop() {
        firebaseAnalytics.logEvent(Event.AD_REWARD_EARNED, mapOf(Key.PLACEMENT to Value.SHOP))
    }

    override fun logCardUnlockedWithCoins(totalUnlocked: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(
            Event.CARD_UNLOCKED,
            mapOf(
                Key.METHOD to Value.COINS,
                Key.TOTAL_UNLOCKED to totalUnlocked,
                Key.TOTAL_COUNT to totalCount,
                Key.COLLECTION_PCT to collectionPct(totalUnlocked, totalCount)
            )
        )
    }

    override fun logCardUnlockedWithAd(totalUnlocked: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(
            Event.CARD_UNLOCKED,
            mapOf(
                Key.METHOD to Value.AD,
                Key.TOTAL_UNLOCKED to totalUnlocked,
                Key.TOTAL_COUNT to totalCount,
                Key.COLLECTION_PCT to collectionPct(totalUnlocked, totalCount)
            )
        )
    }

    private fun collectionPct(unlocked: Int, total: Int): Int =
        if (total <= 0) 0 else ((unlocked * 100) / total).coerceIn(0, 100)

    override fun logShopOpenedFromCollection() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, mapOf(Key.SOURCE to Value.COLLECTION))
    }

    override fun logShopOpenedFromInsufficientCoins() {
        firebaseAnalytics.logEvent(
            Event.SHOP_OPENED,
            mapOf(Key.SOURCE to Value.INSUFFICIENT_COINS)
        )
    }

    override fun logShopOpenedFromEndGame() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, mapOf(Key.SOURCE to Value.END_GAME))
    }

    override fun logShopOpenedFromDailyReward() {
        firebaseAnalytics.logEvent(Event.SHOP_OPENED, mapOf(Key.SOURCE to Value.DAILY_REWARD))
    }

    override fun logCollectionOpenedFromLockedBoard() {
        firebaseAnalytics.logEvent(
            Event.COLLECTION_VIEWED,
            mapOf(Key.SOURCE to Value.LOCKED_BOARD)
        )
    }

    override fun logPurchaseCompleted(product: String, priceMicros: Long, currencyCode: String) {
        firebaseAnalytics.logEvent(
            Event.PURCHASE,
            mapOf(
                Key.PRODUCT to product,
                Key.ITEM_ID to product,
                Key.VALUE to priceMicros / MICROS_PER_UNIT,
                Key.CURRENCY to currencyCode
            )
        )
    }

    override fun logPurchaseFailed() {
        firebaseAnalytics.logEvent(Event.PURCHASE_FAILED)
    }

    override fun logDailyStreakCollected(streakDay: Int, coinsEarned: Long) {
        firebaseAnalytics.logEvent(
            Event.DAILY_STREAK_COLLECTED,
            mapOf(
                Key.STREAK_DAY to streakDay,
                Key.COINS_EARNED to coinsEarned
            )
        )
    }

    override fun logCollectionViewed(unlockedCount: Int, totalCount: Int) {
        firebaseAnalytics.logEvent(
            Event.COLLECTION_VIEWED,
            mapOf(
                Key.UNLOCKED_COUNT to unlockedCount,
                Key.TOTAL_COUNT to totalCount
            )
        )
    }

    override fun logShareClicked(isRewardAvailable: Boolean) {
        firebaseAnalytics.logEvent(
            Event.SHARE_CLICKED,
            mapOf(Key.REWARD_AVAILABLE to isRewardAvailable)
        )
    }

    override fun logNotificationEnabled(isEnabled: Boolean) {
        firebaseAnalytics.logEvent(
            Event.NOTIFICATION_ENABLED,
            mapOf(Key.IS_ENABLED to isEnabled)
        )
    }

    override fun logAppOpened(notificationType: String?, shortcutId: String?) {
        firebaseAnalytics.logEvent(
            Event.APP_OPENED,
            buildMap {
                notificationType?.let { put(Key.NOTIFICATION_TYPE, it) }
                shortcutId?.takeIf { it in ALLOWED_SHORTCUT_IDS }
                    ?.let { put(Key.SHORTCUT_ID, it) }
            }
        )
    }

    override fun logMoreAppsClicked() {
        firebaseAnalytics.logEvent(Event.MORE_APPS_CLICKED)
    }

    override fun logCrossPromotionAppOpened(appId: String) {
        firebaseAnalytics.logEvent(
            Event.CROSS_PROMOTION_APP_CLICKED,
            mapOf(Key.APP_ID to appId, Key.ACTION to Value.OPEN)
        )
    }

    override fun logCrossPromotionStoreOpened(appId: String) {
        firebaseAnalytics.logEvent(
            Event.CROSS_PROMOTION_APP_CLICKED,
            mapOf(Key.APP_ID to appId, Key.ACTION to Value.STORE)
        )
    }

    override fun logAdShown(placement: String) {
        firebaseAnalytics.logEvent(Event.AD_SHOWN, mapOf(Key.PLACEMENT to placement))
    }

    override fun logAdDismissed(placement: String, wasRewardGranted: Boolean) {
        firebaseAnalytics.logEvent(
            Event.AD_DISMISSED,
            mapOf(
                Key.PLACEMENT to placement,
                Key.WAS_REWARD_GRANTED to wasRewardGranted
            )
        )
    }

    override fun logAdImpression(
        valueMicros: Long,
        currencyCode: String,
        adFormat: String,
        adUnitName: String,
        adSource: String?
    ) {
        firebaseAnalytics.logEvent(
            Event.AD_IMPRESSION,
            buildMap {
                put(Key.AD_PLATFORM, Value.AD_PLATFORM_ADMOB)
                put(Key.AD_FORMAT, adFormat)
                put(Key.AD_UNIT_NAME, adUnitName)
                put(Key.CURRENCY, currencyCode)
                put(Key.VALUE, valueMicros / MICROS_PER_UNIT)
                adSource?.let { put(Key.AD_SOURCE, it) }
            }
        )
    }

    override fun logInsufficientCoinsShown(coinsBalance: Long, cardCost: Int) {
        firebaseAnalytics.logEvent(
            Event.INSUFFICIENT_COINS_SHOWN,
            mapOf(
                Key.COINS_BALANCE to coinsBalance,
                Key.CARD_COST to cardCost
            )
        )
    }

    override fun logAppSessionStart(totalGamesPlayed: Long, unlockedCardsCount: Int) {
        firebaseAnalytics.logEvent(
            Event.APP_SESSION_START,
            mapOf(
                Key.TOTAL_GAMES_PLAYED to totalGamesPlayed,
                Key.UNLOCKED_COUNT to unlockedCardsCount
            )
        )
    }

    override fun logLanguageChanged(fromLanguage: String, toLanguage: String) {
        firebaseAnalytics.logEvent(
            Event.LANGUAGE_CHANGED,
            mapOf(
                Key.FROM_LANGUAGE to fromLanguage,
                Key.TO_LANGUAGE to toLanguage
            )
        )
    }

    override fun logCardPairDetailsViewed() {
        firebaseAnalytics.logEvent(Event.CARD_PAIR_DETAILS_VIEWED)
    }

    override fun setUserLanguage(languageTag: String) {
        firebaseAnalytics.setUserProperty(Key.LANGUAGE, languageTag)
    }

    override fun setNotificationPermission(isEnabled: Boolean) {
        firebaseAnalytics.setUserProperty(Key.NOTIFICATION_PERMISSION, isEnabled.toString())
    }

    override fun logLeaderboardOpened() {
        firebaseAnalytics.logEvent(Event.LEADERBOARD_OPENED)
    }

    override fun logDailyChallengeStart(epochDay: Long) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_STARTED,
            mapOf(Key.CHALLENGE_NUMBER to epochDay)
        )
    }

    override fun logDailyChallengeComplete(
        epochDay: Long,
        mistakeCount: Int,
        starCount: Int,
        timeMillis: Long,
        totalGamesCount: Long?
    ) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_COMPLETED,
            buildMap {
                put(Key.CHALLENGE_NUMBER, epochDay)
                put(Key.MISMATCH_COUNT, mistakeCount)
                put(Key.STAR_COUNT, starCount)
                put(Key.TIME_MILLIS, timeMillis)
                totalGamesCount?.let { put(Key.TOTAL_GAMES_COUNT, it) }
            }
        )
    }

    override fun logDailyChallengeShare(epochDay: Long, starCount: Int) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_SHARED,
            mapOf(
                Key.CHALLENGE_NUMBER to epochDay,
                Key.STAR_COUNT to starCount
            )
        )
    }

    override fun logDailyChallengeAlreadyPlayed(epochDay: Long) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_ALREADY_PLAYED,
            mapOf(Key.CHALLENGE_NUMBER to epochDay)
        )
    }

    override fun logDailyChallengeAbandoned(epochDay: Long) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_ABANDONED,
            mapOf(Key.CHALLENGE_NUMBER to epochDay)
        )
    }

    override fun logDailyChallengeHistoryOpened() {
        firebaseAnalytics.logEvent(Event.DAILY_CHALLENGE_HISTORY_OPENED)
    }

    override fun logDailyChallengeHistoryShareClicked(epochDay: Long) {
        firebaseAnalytics.logEvent(
            Event.DAILY_CHALLENGE_HISTORY_SHARED,
            mapOf(Key.CHALLENGE_NUMBER to epochDay)
        )
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
    const val PURCHASE = "purchase"
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
    const val AD_IMPRESSION = "ad_impression"
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
    const val ITEM_ID = "item_id"
    const val VALUE = "value"
    const val CURRENCY = "currency"
    const val AD_PLATFORM = "ad_platform"
    const val AD_FORMAT = "ad_format"
    const val AD_UNIT_NAME = "ad_unit_name"
    const val AD_SOURCE = "ad_source"
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
