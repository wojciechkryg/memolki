package com.wojdor.memolki.util.notification

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.notification_ad_reward_bodies
import com.wojdor.memolki.shared.resources.notification_ad_reward_titles
import com.wojdor.memolki.shared.resources.notification_daily_challenge_bodies
import com.wojdor.memolki.shared.resources.notification_daily_challenge_titles
import com.wojdor.memolki.shared.resources.notification_daily_streak_bodies
import com.wojdor.memolki.shared.resources.notification_daily_streak_titles
import com.wojdor.memolki.shared.resources.notification_reminder_bodies
import com.wojdor.memolki.shared.resources.notification_reminder_titles
import com.wojdor.memolki.util.resource.StringProvider
import org.jetbrains.compose.resources.StringArrayResource

enum class NotificationCopy(
    val titlesResource: StringArrayResource,
    val bodiesResource: StringArrayResource
) {
    REMINDER(Res.array.notification_reminder_titles, Res.array.notification_reminder_bodies),
    AD_REWARD(Res.array.notification_ad_reward_titles, Res.array.notification_ad_reward_bodies),
    STREAK(Res.array.notification_daily_streak_titles, Res.array.notification_daily_streak_bodies),
    DAILY_CHALLENGE(
        Res.array.notification_daily_challenge_titles,
        Res.array.notification_daily_challenge_bodies
    )
}

class NotificationStrings(private val stringProvider: StringProvider) {

    suspend fun titles(copy: NotificationCopy): List<String> =
        stringProvider.getStringArray(copy.titlesResource)

    suspend fun bodies(copy: NotificationCopy): List<String> =
        stringProvider.getStringArray(copy.bodiesResource)
}
